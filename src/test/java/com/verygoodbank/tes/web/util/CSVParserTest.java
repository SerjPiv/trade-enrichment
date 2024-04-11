package com.verygoodbank.tes.web.util;

import com.verygoodbank.tes.web.domain.Product;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CSVParserTest {

    @Test
    public void shouldParseProductCsv() {
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .productId(1)
                .productName("Treasury Bills Domestic")
                .build());
        products.add(Product.builder()
                .productId(2)
                .productName("Corporate Bonds Domestic")
                .build());

        assertThat(CSVParser.parseProductCsv(readCsvToFile("test_product.csv")))
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(products);
    }

    private File readCsvToFile(String fileName) {
        return new File(getClass().getClassLoader().getResource(fileName).getFile());
    }
}
