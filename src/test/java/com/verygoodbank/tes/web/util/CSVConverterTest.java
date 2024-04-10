package com.verygoodbank.tes.web.util;

import com.verygoodbank.tes.web.domain.Trade;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CSVConverterTest {

    @Test
    public void shouldConvertTradesToStringCsv() {
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

        String expectedCsv = """
                date,product_name,currency,price
                20160101,Treasury Bills Domestic,EUR,10.0
                20160101,Corporate Bonds Domestic,EUR,20.1
                """;

        assertThat(CSVConverter.convertToCsv(trades))
                .isEqualTo(expectedCsv);
    }

    @Test
    public void shouldSkipConvertingTradesMarkedAsSkippedToStringCsv() {
        List<Trade> trades = new ArrayList<>();
        trades.add(Trade.builder()
                .date("20160101")
                .productId(1)
                .currency("EUR")
                .price(10.0)
                .skip(false)
                .productName("Treasury Bills Domestic")
                .build());
        trades.add(Trade.builder()
                .date("20160101")
                .productId(2)
                .currency("EUR")
                .price(20.1)
                .skip(false)
                .productName("Corporate Bonds Domestic")
                .build());
        trades.add(Trade.builder()
                .date("20160101")
                .productId(3)
                .currency("EUR")
                .price(20.1)
                .skip(true)
                .productName("Test Domestic")
                .build());

        String expectedCsv = """
                date,product_name,currency,price
                20160101,Treasury Bills Domestic,EUR,10.0
                20160101,Corporate Bonds Domestic,EUR,20.1
                """;

        assertThat(CSVConverter.convertToCsv(trades))
                .isEqualTo(expectedCsv);
    }
}
