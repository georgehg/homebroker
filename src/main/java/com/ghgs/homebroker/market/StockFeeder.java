package com.ghgs.homebroker.market;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StockFeeder {
    
    private ObjectMapper objectMapper = new ObjectMapper();

    private RestTemplate restTemplate = new RestTemplateBuilder().build();

    private String financeApi = "https://query1.finance.yahoo.com/v8/finance/chart/";

    Stock getPrice(String ticker) {
        String response = restTemplate.getForObject(financeApi + ticker, String.class);
        
        try {
            JsonNode jsonResponse = objectMapper.readTree(response);

            JsonNode marketPricNode = jsonResponse
                .path("chart")
                .withArray("result").get(0)
                .path("meta")
                .path("regularMarketPrice");

            return new Stock(ticker, marketPricNode.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }   
    }
}
