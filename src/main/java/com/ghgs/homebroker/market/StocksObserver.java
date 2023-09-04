package com.ghgs.homebroker.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ghgs.homebroker.client.IClient;

@Component
@EnableScheduling
public class StocksObserver {

    Logger logger = LoggerFactory.getLogger(StocksObserver.class);

    @Autowired
    private StockFeeder stockFeeder;

    private Map<String, List<IClient>> stocksSubscribers = new HashMap<>();

    private ForkJoinPool threadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 100);

    @Scheduled(fixedDelay = 5000)
    public void marketFeed() {
        logger.info("Starting Market feeder.");
        threadPool.submit(() ->
            stocksSubscribers.keySet()
                        .parallelStream()
                        .map(stockFeeder::getPrice)
                        .forEach(this::feedClients));
    }

    private void feedClients(Stock stock) {
        threadPool.submit(() ->
            stocksSubscribers.get(stock.getTicker())
                            .parallelStream()
                            .forEach(client -> safeFeedClient(client, (stock))));
    }

    private void safeFeedClient(IClient client, Stock stock) {
        try {
            logger.info("Notifying " + client + ". Stock: " + stock.getTicker());
            client.notify(stock);
        } catch (Exception ex) {
            logger.warn("Unsubscribing " + client);
            unsubscribe(client);
            client.close(ex);
        }
    }

    public void subscribe(IClient client) {
        for (String ticker : client.getTickers()) {
            stocksSubscribers.compute(ticker, (tck, clients) -> {
                if (Objects.isNull(clients)) {
                    ArrayList<IClient> newList = new ArrayList<>();
                    newList.add(client);
                    return Collections.synchronizedList(newList);
                } else {
                    clients.add(client);
                    return clients;
                }
            });
        }
    }

    public void unsubscribe(IClient client) {
        client.getTickers()
            .stream()
            .forEach(ticker -> this.removeClient(ticker, client));
    }

    private void removeClient(String ticker, IClient client) {
        stocksSubscribers.get(ticker).remove(client);
    }

}
