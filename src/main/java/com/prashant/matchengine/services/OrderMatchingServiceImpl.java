package com.prashant.matchengine.services;

import com.prashant.matchengine.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Service
public class OrderMatchingServiceImpl implements OrderMatchingService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final ConcurrentNavigableMap<Integer, BidOffer> bidOffers;
    private final ConcurrentNavigableMap<Integer, AskOffer> askOffers;
    private final ConcurrentNavigableMap<Integer, Order> cancelledOrRejectedOrders;
    private final ConcurrentNavigableMap<Integer, BidOffer> bidMatchOffers;
    private final ConcurrentNavigableMap<Integer, AskOffer> askMatchOffers;

    private final OrderConsumer orderConsumer;
    private final OrderProducer orderProducer;


    // first-in-first-out
    @Autowired
    public OrderMatchingServiceImpl(OrderProducer orderProducer, OrderConsumer orderConsumer) {
        this.bidOffers = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
        this.askOffers = new ConcurrentSkipListMap<>(Integer::compareTo);
        this.cancelledOrRejectedOrders = new ConcurrentSkipListMap<>(Integer::compareTo);
        this.bidMatchOffers = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
        this.askMatchOffers = new ConcurrentSkipListMap<>(Integer::compareTo);

        this.orderProducer = orderProducer;
        this.orderConsumer = orderConsumer;

        this.orderConsumer.Subscribe(order -> {
            LOGGER.info("Order matching process started for an order " + order);

            if (order.getStatus().equals(OrderStatus.CANCELLED)) {
                cancelledOrRejectedOrders.put(order.getOrderno(), order);
            }  else {
                matchOrder(order);
                 if (order.getStatus().equals(OrderStatus.MATCHED)) {
                     if (order.getType().equals(Side.BUY)) {
                         bidMatchOffers.put(order.getOrderno(), (BidOffer) order);
                     } else {
                         askMatchOffers.put(order.getOrderno(), (AskOffer) order);
                     }
                } else {
                     if (order.getQty() > 0) {
                         if (order.getType().equals(Side.BUY)) {
                             bidOffers.put(order.getOrderno(), (BidOffer) order);
                            } else {
                             askOffers.put(order.getOrderno(), (AskOffer) order);
                            }
                     } else {
                         order.setOrderStatus(OrderStatus.REJECTED);
                         cancelledOrRejectedOrders.put(order.getOrderno(), order);
                     }
                 }
            }
            LOGGER.info("Finished order matching process started for an order " + order);
        });
    }

    @Override
    public void putBidOffer(BidOffer bidOffer) {
        bidOffer.setOrderStatus(OrderStatus.NEW);
        orderProducer.publish(bidOffer);
    }

    @Override
    public void putAskOffer(AskOffer askOffer) {
        askOffer.setOrderStatus(OrderStatus.NEW);
        orderProducer.publish(askOffer);
    }

    @Override
    public Security getSecurity() {
        Security security = new Security(bidOffers.values(), askOffers.values(), cancelledOrRejectedOrders.values());
        return security;
    }

    @Override
    public MatchOffers getMatch() {
        MatchOffers match = new MatchOffers(bidMatchOffers.values(), askMatchOffers.values());
        return match;
    }

    @Override
    public void matchOrder(Order order) {
        switch (order.getType())
        {
            case BUY:
                matchBuyOrder(order);
                break;
            case SELL:
                matchSellOrder(order);
                break;
            default:
                LOGGER.error ("Offer not supported!");
        }
    }

    private void matchBuyOrder(Order buyOrder) {
        Iterator iterator = askOffers.keySet().iterator();
        while(iterator.hasNext()) {
            Integer orderNumber = (Integer) iterator.next();
            AskOffer askOffer = askOffers.get(orderNumber);
            if(buyOrder.getQty() > 0 && buyOrder.getPrc() >= askOffer.getPrc()) {
                askOffers.remove(askOffer.getOrderno());
                if(buyOrder.getQty() > askOffer.getQty()) {
                  buyOrder.setOrderStatus(OrderStatus.PARTIALLY_MATCHED);
                  buyOrder.setQty(buyOrder.getQty() - askOffer.getQty());
                } else if (buyOrder.getQty() == askOffer.getQty()) {
                    buyOrder.setOrderStatus(OrderStatus.MATCHED);
                } else {
                buyOrder.setOrderStatus(OrderStatus.PARTIALLY_MATCHED);
                askOffer.setQty(askOffer.getQty() - buyOrder.getQty());
                   buyOrder.setQty(0);
                    askOffers.put(askOffer.getOrderno(), askOffer);
                }
            }
        }
    }

    private void matchSellOrder(Order sellOrder) {
        Iterator iterator = bidOffers.keySet().iterator();
        while(iterator.hasNext()) {
             Integer orderNumber = (Integer) iterator.next();
            BidOffer bidOffer = bidOffers.get(orderNumber);
            if(sellOrder.getQty() > 0 && sellOrder.getPrc() <= bidOffer.getPrc()) {
                bidOffers.remove(bidOffer.getOrderno());
                if(sellOrder.getQty() > bidOffer.getQty()) {
                   sellOrder.setOrderStatus(OrderStatus.PARTIALLY_MATCHED);
                   sellOrder.setQty(sellOrder.getQty() - bidOffer.getQty());
                }  else if (sellOrder.getQty() == bidOffer.getQty()) {
                    sellOrder.setOrderStatus(OrderStatus.MATCHED);
                } else {
                    sellOrder.setOrderStatus(OrderStatus.PARTIALLY_MATCHED);
                    bidOffer.setQty(bidOffer.getQty() - sellOrder.getQty());
                    sellOrder.setQty(0);
                    bidOffers.put(bidOffer.getOrderno(), bidOffer);
                }
            }
        }
    }

    public void cancelOrder( int orderNumber) {
        if (bidOffers.containsKey(orderNumber)) {
            bidOffers.get(orderNumber).setOrderStatus(OrderStatus.CANCELLED);
            LOGGER.info("Order # " + orderNumber + " has been cancelled ..." );
            orderProducer.publish(bidOffers.get(orderNumber));
            bidOffers.remove(orderNumber);
        } else if (askOffers.containsKey(orderNumber)) {
            askOffers.get(orderNumber).setOrderStatus(OrderStatus.CANCELLED);
            LOGGER.info("Order # " + orderNumber + " has been cancelled ..." );
            orderProducer.publish(askOffers.get(orderNumber));
            askOffers.remove(orderNumber);
        }
    }
}
