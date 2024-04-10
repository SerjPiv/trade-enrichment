package com.verygoodbank.tes.web.configuration;

import com.verygoodbank.tes.web.domain.TradeEnrichmentService;
import com.verygoodbank.tes.web.service.DefaultTradeEnrichmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TradeEnrichmentServiceConfiguration {

    @Bean
    public TradeEnrichmentService defaultTradeEnrichmentService() {
        return new DefaultTradeEnrichmentService("product.csv");
    }
}
