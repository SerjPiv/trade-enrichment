package com.verygoodbank.tes.web.configuration;

import com.verygoodbank.tes.web.domain.ProductService;
import com.verygoodbank.tes.web.domain.TradeEnrichmentService;
import com.verygoodbank.tes.web.service.DefaultProductService;
import com.verygoodbank.tes.web.service.DefaultTradeEnrichmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TradeEnrichmentServiceConfiguration {

    @Bean
    public TradeEnrichmentService defaultTradeEnrichmentService(ProductService defaultProductService) {
        return new DefaultTradeEnrichmentService(defaultProductService);
    }

    @Bean
    public ProductService defaultProductService(@Value("${product.file.name}") String productFileName) {
        return new DefaultProductService(productFileName);
    }
}
