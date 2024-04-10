package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.web.TestUtils;
import com.verygoodbank.tes.web.domain.Trade;
import com.verygoodbank.tes.web.domain.TradeEnrichmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {TradeEnrichmentController.class},
        properties = {"server.servlet.context-path=/"})
public class TradeEnrichmentControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private TradeEnrichmentService tradeEnrichmentService;

    @Test
    public void shouldEnrichTrades() throws Exception {

        when(tradeEnrichmentService.enrichTrades(trades()))
                .thenReturn(enrichedTrades());

        String expectedCsv = """
                date,product_name,currency,price
                20160101,Treasury Bills Domestic,EUR,10.0
                20160101,Corporate Bonds Domestic,EUR,20.1
                """;

        mockMvc.perform(multipart("/api/v1/enrich").file(TestUtils.readCsvToMultipartFile("test_trade.csv")))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCsv));
    }

    @Test
    public void shouldReturnBadRequestIfCsvIsNull() throws Exception {

        mockMvc.perform(multipart("/api/v1/enrich").file(TestUtils.readCsvToMultipartFile("not_existing.csv")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The file is empty or null. Please provide a valid file."));
    }

    @Test
    public void shouldReturnInternalErrorIfException() throws Exception {

        doThrow(new RuntimeException("An error occurred while processing the file."))
                .when(tradeEnrichmentService).enrichTrades(trades());

        mockMvc.perform(multipart("/api/v1/enrich").file(TestUtils.readCsvToMultipartFile("test_trade.csv")))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while processing the file."));
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
}
