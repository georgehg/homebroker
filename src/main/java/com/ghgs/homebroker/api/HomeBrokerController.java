package com.ghgs.homebroker.api;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.ghgs.homebroker.client.PriceEmitter;
import com.ghgs.homebroker.market.StocksObserver;

@RestController
@RequestMapping("/homebroker")
public class HomeBrokerController{

    @Autowired
    private StocksObserver stocksObserver;

    @GetMapping(path = "/prices", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter stockPrices(@RequestParam(value = "tickers")  String tickers) {
        String[] tickerList = tickers.split(",");
        return stocksObserver.subscribe(new PriceEmitter(Arrays.asList(tickerList)));
    }

}
