package com.verygoodbank.tes.web.util;

import com.opencsv.CSVReaderHeaderAware;
import com.verygoodbank.tes.web.domain.Product;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class CSVParser {

    public static List<Product> parseProductCsv(File file) {
        List<Product> list = new ArrayList<>();
        try (Reader reader = getReader(file)) {
            try (CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    list.add(Product.builder()
                            .productId(Long.parseLong(line[0]))
                            .productName(line[1])
                            .build());
                }
            }
        } catch (Exception e) {
            log.error(String.format("Error parsing product CSV file: %s", file.getName()), e);
            throw new RuntimeException(e);
        }
        return list;
    }

    private Reader getReader(File file) {
        try {
            return new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
