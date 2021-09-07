package com.prashant.matchengine.services;


import com.prashant.matchengine.models.Order;

public interface OrderProducer {
    void publish(Order order);
}
