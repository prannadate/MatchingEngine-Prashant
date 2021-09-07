package com.prashant.matchengine.models;

import java.util.Collection;

public class Security {
    private Collection<BidOffer> buys;
    private Collection<AskOffer> sells;
    private Collection<Order> cancel_rejected;

    public Security(Collection<BidOffer> buys, Collection<AskOffer> sells, Collection<Order> cancel_rejected) {
        this.buys = buys;
        this.sells = sells;
        this.cancel_rejected = cancel_rejected;
    }

    public Collection<BidOffer> getBuys() {
        return buys;
    }

    public void setBuys(Collection<BidOffer> buys) {
        this.buys = buys;
    }

    public Collection<AskOffer> getSells() {
        return sells;
    }

    public void setSells(Collection<AskOffer> sells) {
        this.sells = sells;
    }

    public Collection<Order> getCancel_rejected() {
        return cancel_rejected;
    }

    public void setCancel_rejected(Collection<Order> cancel_rejected) {
        this.cancel_rejected = cancel_rejected;
    }
}
