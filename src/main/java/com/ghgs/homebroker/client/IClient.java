package com.ghgs.homebroker.client;

public interface IClient {

    void notify(String price) throws Exception;

    void close(Throwable cause);
    
}
