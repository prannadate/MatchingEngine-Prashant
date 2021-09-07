package com.prashant.matchengine.services;

import com.prashant.matchengine.models.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderMatchingServiceImplTest {
    private OrderMatchingService orderMatchingService;
    private LinkedBlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    @Before
    public void setup() {
        orderMatchingService = new OrderMatchingServiceImpl(new OrderProducerImpl(queue), new OrderConsumerImpl(queue));
    }

    @Test
    public void testOrderMatchingLogic() {
        orderMatchingService.putAskOffer(new AskOffer(10, 15));
        orderMatchingService.putAskOffer(new AskOffer(10, 13));
        orderMatchingService.putBidOffer(new BidOffer(10, 7));
        orderMatchingService.putBidOffer(new BidOffer(10, 9.5));

        Security security = orderMatchingService.getSecurity();
        System.out.println("buys: " + Arrays.toString(security.getBuys().toArray()));
        System.out.println("sells: " + Arrays.toString(security.getSells().toArray()));

        orderMatchingService.putAskOffer(new AskOffer(5, 9.5));
        security = orderMatchingService.getSecurity();
        System.out.println();
        System.out.println("buys: " + Arrays.toString(security.getBuys().toArray()));
        System.out.println("sells: " + Arrays.toString(security.getSells().toArray()));

        orderMatchingService.putBidOffer(new BidOffer(6, 13));
        security = orderMatchingService.getSecurity();
        System.out.println();
        System.out.println("buys: " + Arrays.toString(security.getBuys().toArray()));
        System.out.println("sells: " + Arrays.toString(security.getSells().toArray()));

        orderMatchingService.putAskOffer(new AskOffer(7, 7 ));
        security = orderMatchingService.getSecurity();
        System.out.println();
        System.out.println("buys: " + Arrays.toString(security.getBuys().toArray()));
        System.out.println("sells: " + Arrays.toString(security.getSells().toArray()));

        orderMatchingService.putAskOffer(new AskOffer(12, 6));

        waitTillAsyncThreadFinishes();

        security = orderMatchingService.getSecurity();
        System.out.println();
        System.out.println("buys: " + Arrays.toString(security.getBuys().toArray()));
        System.out.println("sells: " + Arrays.toString(security.getSells().toArray()));
    }

    private void waitTillAsyncThreadFinishes() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}