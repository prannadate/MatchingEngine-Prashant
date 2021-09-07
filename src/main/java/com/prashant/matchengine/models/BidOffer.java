package com.prashant.matchengine.models;

public class BidOffer extends Order {
    public BidOffer() {
        super(Side.BUY);
    }

    public BidOffer(long qty, double prc) {
        super(qty, prc, Side.BUY);
    }
}
