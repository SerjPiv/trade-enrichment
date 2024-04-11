package com.verygoodbank.tes.web.configuration;

import com.verygoodbank.tes.web.domain.ProductService;
import com.verygoodbank.tes.web.service.DefaultProductService;
import com.verygoodbank.tes.web.service.StreamingTradeEnrichmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TradeEnrichmentServiceConfiguration {

    @Bean
    public StreamingTradeEnrichmentService streamingTradeEnrichmentService(ProductService defaultProductService) {
        return new StreamingTradeEnrichmentService(defaultProductService);
    }

    @Bean
    public ProductService defaultProductService(@Value("${product.file.name}") String productFileName) {
        return new DefaultProductService(productFileName);
    }
}
