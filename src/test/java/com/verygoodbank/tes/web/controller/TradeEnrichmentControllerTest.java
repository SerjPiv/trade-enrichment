package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.web.TestUtils;
import com.verygoodbank.tes.web.service.StreamingTradeEnrichmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

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
    private StreamingTradeEnrichmentService streamingTradeEnrichmentService;

    @Test
    public void shouldEnrichTrades() throws Exception {

        MockMultipartFile file = TestUtils.readCsvToMultipartFile("test_trade.csv");

        String expectedCsv = """
                date,product_name,currency,price
                20160101,Treasury Bills Domestic,EUR,10.0
                20160101,Corporate Bonds Domestic,EUR,20.1
                """;

        when(streamingTradeEnrichmentService.enrichTrades(file))
                .thenReturn(expectedCsv);


        mockMvc.perform(multipart("/api/v1/enrich").file(file))
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

        MockMultipartFile file = TestUtils.readCsvToMultipartFile("test_trade.csv");

        doThrow(new RuntimeException("An error occurred while processing the file."))
                .when(streamingTradeEnrichmentService).enrichTrades(file);

        mockMvc.perform(multipart("/api/v1/enrich").file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while processing the file."));
    }
}
