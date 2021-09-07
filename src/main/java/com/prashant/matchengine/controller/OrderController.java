package com.prashant.matchengine.controller;

import com.prashant.matchengine.models.*;
import com.prashant.matchengine.services.OrderMatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class OrderController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderMatchingService orderMatchingService;

    @RequestMapping("/security")
    public Security getSecurity() {
        LOGGER.info("processing security request!");
        Security security = orderMatchingService.getSecurity();
        return security;
    }

    @RequestMapping("/match")
    public MatchOffers getMatch() {
        LOGGER.info("processing match request!");
        MatchOffers match = orderMatchingService.getMatch();
        return match;
    }

    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    public ResponseEntity buy(@RequestBody BidOffer bidOffer) {
        orderMatchingService.putBidOffer(bidOffer);

        return new ResponseEntity(HttpStatus.OK);
    }
    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public ResponseEntity sell(@RequestBody AskOffer askOffer) {
        orderMatchingService.putAskOffer(askOffer);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/cancel/{ordernumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity cancel(@PathVariable("ordernumber") int orderNumber) {
        orderMatchingService.cancelOrder(orderNumber);

        return new ResponseEntity(HttpStatus.OK);
    }
}
