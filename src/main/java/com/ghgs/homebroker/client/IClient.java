package com.ghgs.homebroker.client;

import java.util.List;

import com.ghgs.homebroker.market.Stock;

public interface IClient {

    List<String> getTickers();

    void notify(Stock stock) throws Exception;

    void close(Throwable cause);
    
}
