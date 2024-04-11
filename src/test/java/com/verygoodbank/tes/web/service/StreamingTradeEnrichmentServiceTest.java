package com.verygoodbank.tes.web.service;

import com.verygoodbank.tes.web.TestUtils;
import com.verygoodbank.tes.web.domain.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
public class StreamingTradeEnrichmentServiceTest {

    private StreamingTradeEnrichmentService streamingTradeEnrichmentService;
    @Mock
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        streamingTradeEnrichmentService = new StreamingTradeEnrichmentService(productService);
    }

    @Test
    public void shouldEnrichTrades() throws IOException {
        when(productService.getProductName(1))
                .thenReturn("Treasury Bills Domestic");
        when(productService.getProductName(2))
                .thenReturn("Corporate Bonds Domestic");

        String expectedCsv = """
                date,product_name,currency,price
                20160101,Treasury Bills Domestic,EUR,10.0
                20160101,Corporate Bonds Domestic,EUR,20.1
                """;

        assertThat(streamingTradeEnrichmentService.enrichTrades(TestUtils.readCsvToMultipartFile("test_trade.csv")))
                .isEqualTo(expectedCsv);
    }

    @Test
    public void shouldSkipTradeIfInvalidDate() throws IOException {
        when(productService.getProductName(1))
                .thenReturn("Treasury Bills Domestic");
        when(productService.getProductName(2))
                .thenReturn("Corporate Bonds Domestic");

        String expectedCsv = """
                date,product_name,currency,price
                20160101,Corporate Bonds Domestic,EUR,20.1
                """;

        assertThat(streamingTradeEnrichmentService.enrichTrades(TestUtils.readCsvToMultipartFile("test_trade_invalid_date.csv")))
                .isEqualTo(expectedCsv);
    }
}
