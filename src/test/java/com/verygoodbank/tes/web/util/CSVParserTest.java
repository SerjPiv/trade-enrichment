package com.verygoodbank.tes.web.util;

import com.verygoodbank.tes.web.TestUtils;
import com.verygoodbank.tes.web.domain.Product;
import com.verygoodbank.tes.web.domain.Trade;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CSVParserTest {

    @Test
    public void shouldParseTradeCsv() throws IOException {
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

        assertThat(CSVParser.parseTradeCsv(TestUtils.readCsvToMultipartFile("test_trade.csv")))
                .containsAll(trades);
    }

    @Test
    public void shouldThrowExceptionIfErrorParsingTradeCsv() {

        assertThatThrownBy(() -> CSVParser.parseTradeCsv(TestUtils.readCsvToMultipartFile("test_trade_corrupted.csv")))
                .isInstanceOf(RuntimeException.class);
    }

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
