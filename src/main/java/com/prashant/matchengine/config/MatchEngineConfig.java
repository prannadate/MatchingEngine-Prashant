package com.prashant.matchengine.config;

import com.prashant.matchengine.models.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class MatchEngineConfig {
    @Bean(name = "sharedQueue")
    public LinkedBlockingQueue<Order> sharedQueue() {
        return new LinkedBlockingQueue<>();
    }

}
