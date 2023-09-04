package com.ghgs.homebroker.client;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.ghgs.homebroker.market.Stock;

public interface IClient {

    List<String> getTickers();

    ResponseBodyEmitter getEmitter();

    void notify(Stock stock) throws Exception;

    void close(Throwable cause);
    
}
