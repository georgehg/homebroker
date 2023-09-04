package com.ghgs.homebroker.client;

import com.ghgs.homebroker.market.Stock;

public interface IClient {

    void notify(Stock stock) throws Exception;

    void close(Throwable cause);
    
}
