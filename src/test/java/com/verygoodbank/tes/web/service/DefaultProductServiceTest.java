package com.verygoodbank.tes.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultProductServiceTest {

    private DefaultProductService defaultProductService;

    @BeforeEach
    public void setup() {
        defaultProductService = new DefaultProductService("test_product.csv");
        defaultProductService.initProductMap();
    }

    @Test
    public void shouldGetProductName() {
        assertThat(defaultProductService.getProductName(1))
                .isEqualTo("Treasury Bills Domestic");
    }

    @Test
    public void shouldGetDefaultProductNameIfNotPresentById() {
        assertThat(defaultProductService.getProductName(99999))
                .isEqualTo("Missing Product Name");
    }
}
