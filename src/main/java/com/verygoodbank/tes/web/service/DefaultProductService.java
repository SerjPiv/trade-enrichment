package com.verygoodbank.tes.web.service;

import com.verygoodbank.tes.web.domain.Product;
import com.verygoodbank.tes.web.domain.ProductService;
import com.verygoodbank.tes.web.util.CSVParser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final String productFileName;
    private volatile ConcurrentHashMap<Long, String> productIdToProductName;

    @Override
    public String getProductName(long productId) {
        String productName = productIdToProductName.get(productId);
        if (productName == null) {
            log.error("Product name is null for productId: {}. Setting default product name [Missing Product Name]", productId);
            return "Missing Product Name";
        }
        return productName;
    }

    @PostConstruct
    public void initProductMap() {
        productIdToProductName = new ConcurrentHashMap<>();
        ClassLoader classLoader = getClass().getClassLoader();
        List<Product> products = CSVParser.parseProductCsv(new File(classLoader.getResource(productFileName).getFile()));
        products.forEach(product -> productIdToProductName.put(product.getProductId(), product.getProductName()));
    }
}
