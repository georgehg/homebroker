package com.ghgs.homebroker.client;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.ghgs.homebroker.market.Stock;

public class PriceEmitter implements IClient {

    private UUID clientId;

    private List<String> tickers;

    private SseEmitter emitter;

    public PriceEmitter(List<String> tickers, SseEmitter emitter) {
        this.clientId = UUID.randomUUID();
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

    public UUID getClientId() {
        return this.clientId;
    }

    public List<String> getTickers() {
        return this.tickers;
    }

    @Override
    public String toString() {
        return "Client: " + this.clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PriceEmitter))
            return false;

        PriceEmitter other = (PriceEmitter)o;

        return this.clientId.equals(other.getClientId());
    }

    @Override
    public final int hashCode() {
        return 31 * 16 + this.clientId.hashCode();
    }

}
