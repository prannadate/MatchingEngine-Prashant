package com.prashant.matchengine.models;

public class AskOffer extends Order {
    public AskOffer() {
        super(Side.SELL);
    }

    public AskOffer(long qty, double prc) {
        super(qty, prc, Side.SELL);
    }
}
