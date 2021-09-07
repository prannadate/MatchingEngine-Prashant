package com.prashant.matchengine.services;

public interface OrderConsumer {
    void Subscribe(OrderListener orderListener);
}
