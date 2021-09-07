package com.prashant.matchengine.services;


import com.prashant.matchengine.models.Order;

public interface OrderListener {
    void onProcess(Order order);
}
