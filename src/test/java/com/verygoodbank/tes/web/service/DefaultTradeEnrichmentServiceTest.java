package com.verygoodbank.tes.web.service;

import com.verygoodbank.tes.web.domain.Trade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultTradeEnrichmentServiceTest {

    private DefaultTradeEnrichmentService defaultTradeEnrichmentService;

    @BeforeEach
    public void setUp() {
        defaultTradeEnrichmentService = new DefaultTradeEnrichmentService("test_product.csv");
        defaultTradeEnrichmentService.initProductMap();
    }

    @Test
    public void shouldEnrichTrades() {
        assertThat(defaultTradeEnrichmentService.enrichTrades(trades()))
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(enrichedTrades());
    }

    @Test
    public void shouldSkipTradeIfInvalidDate() {
        assertThat(defaultTradeEnrichmentService.enrichTrades(tradesWithInvalidDate()))
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(List.of(Trade.builder()
                                .date("20160101")
                                .productId(2)
                                .currency("EUR")
                                .price(20.1)
                                .productName("Corporate Bonds Domestic")
                                .skip(false)
                                .build(),
                        Trade.builder()
                                .date("20160101834")
                                .productId(1)
                                .currency("EUR")
                                .price(10.0)
                                .skip(true)
                                .build()));
    }

    @Test
    public void shouldSetDefaultTradeNameIfMissingInDictionary() {
        assertThat(defaultTradeEnrichmentService.enrichTrades(tradesWithMissingDictionaryName()))
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(List.of(Trade.builder()
                                .date("20160101")
                                .productId(100000)
                                .currency("EUR")
                                .price(10.0)
                                .productName("Missing Product Name")
                                .build(),
                        Trade.builder()
                                .date("20160101")
                                .productId(2)
                                .currency("EUR")
                                .price(20.1)
                                .productName("Corporate Bonds Domestic")
                                .build()));
    }

    private List<Trade> enrichedTrades() {
        List<Trade> trades = new ArrayList<>();
        trades.add(Trade.builder()
                .date("20160101")
                .productId(1)
                .currency("EUR")
                .price(10.0)
                .productName("Treasury Bills Domestic")
                .build());
        trades.add(Trade.builder()
                .date("20160101")
                .productId(2)
                .currency("EUR")
                .price(20.1)
                .productName("Corporate Bonds Domestic")
                .build());
        return trades;
    }

    private List<Trade> trades() {
        List<Trade> trades = new ArrayList<>();
        trades.add(Trade.builder()
                .date("20160101")
                .productId(1)
                .currency("EUR")
                .price(10.0)
                .build());
        trades.add(Trade.builder()
                .date("20160101")
                .productId(2)
                .currency("EUR")
                .price(20.1)
                .build());
        return trades;
    }

    private List<Trade> tradesWithInvalidDate() {
        List<Trade> trades = new ArrayList<>();
        trades.add(Trade.builder()
                .date("20160101834")
                .productId(1)
                .currency("EUR")
                .price(10.0)
                .build());
        trades.add(Trade.builder()
                .date("20160101")
                .productId(2)
                .currency("EUR")
                .price(20.1)
                .build());
        return trades;
    }

    private List<Trade> tradesWithMissingDictionaryName() {
        List<Trade> trades = new ArrayList<>();
        trades.add(Trade.builder()
                .date("20160101")
                .productId(100000)
                .currency("EUR")
                .price(10.0)
                .build());
        trades.add(Trade.builder()
                .date("20160101")
                .productId(2)
                .currency("EUR")
                .price(20.1)
                .build());
        return trades;
    }
}
