package com.prashant.matchengine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public abstract class Order {
    private int orderno;
    private UUID id;
    private long qty;
    private double prc;
    @JsonIgnore
    private final Side type;
    private OrderStatus status;



    public Order(Side type) {
        this.id = UUID.randomUUID();
        this.type = type;
    }

    public Order(long qty, double prc, Side type) {
        this.id = UUID.randomUUID();
        this.qty = qty;
        this.prc = prc;
        this.type = type;
        }

    public Order(UUID id, long qty, double prc, Side type) {
        this.id = id;
        this.qty = qty;
        this.prc = prc;
        this.type = type;
        }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }

    public double getPrc() {
        return prc;
    }

    public void setPrc(double prc) {
        this.prc = prc;
    }

    public Side getType() {
        return type;
    }

    public OrderStatus getStatus(){ return status;}

    public void setOrderStatus(OrderStatus status) {
        this.status = status;
    }

    public int getOrderno() { return orderno;}

    public void setOrderno(int orderno) {
        this.orderno = orderno;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getId().hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (obj.getClass() != this.getClass()))
            return false;

        Order order = (Order) obj;
        return getId().equals(order.getId());
    }

    @Override
    public String toString() {
        //return "qty: " + getQty() + " prc: " + getPrc() + " id: " + getId() + " date: " + getCurrentTime() + " type: " + getType();
        return "orderno: " + getOrderno() + "qty: " + getQty() + " prc: " + getPrc() + " status: " + getStatus();
    }
}