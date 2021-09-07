package com.prashant.matchengine.services;


import com.prashant.matchengine.models.*;

public interface OrderMatchingService {
    void putBidOffer(BidOffer bidOffer);
    void putAskOffer(AskOffer askOffer);
    Security getSecurity();
    void matchOrder(Order order);
    void cancelOrder(int ordernumber);
    MatchOffers getMatch();

}
