package com.ghgs.homebroker.client;

import java.time.LocalTime;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.ghgs.homebroker.market.Stock;

public class PriceEmitter implements IClient {

    private String tickers;

    private SseEmitter emitter;

    public PriceEmitter(String tickers, SseEmitter emitter) {
        this.tickers = tickers;
        this.emitter = emitter;
    }

    @Override
    public void notify(Stock stock) throws Exception {
        SseEventBuilder event = SseEmitter.event()
            .data("Symbol: " + stock.getTicker() + ". Price: " + stock.getPrice() + ". Time: "  + LocalTime.now().toString())
            .id(String.valueOf(this.hashCode()))
            .name("Stock Price");
            
        emitter.send(event);
    }

    @Override
    public void close(Throwable cause) {
        emitter.completeWithError(cause);
    }

    @Override
    public String toString() {
        return "Client: " + emitter.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PriceEmitter))
            return false;

        PriceEmitter other = (PriceEmitter)o;

        return this.equals(other);
    }

    @Override
    public final int hashCode() {
        return 31 * 16 +
            this.emitter.toString().hashCode() +
            this.tickers.hashCode();
    }

}
