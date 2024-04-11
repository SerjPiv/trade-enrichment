package com.verygoodbank.tes.web.service;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.verygoodbank.tes.web.domain.ProductService;
import com.verygoodbank.tes.web.util.DateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

@Slf4j
@RequiredArgsConstructor
public class StreamingTradeEnrichmentService {

    private final ProductService productService;

    public String enrichTrades(MultipartFile file) {
        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new com.opencsv.CSVWriter(stringWriter)) {
            writeCsvHeader(csvWriter);
            try (Reader reader = new InputStreamReader(file.getInputStream())) {
                try (CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(reader)) {
                    processTrades(csvWriter, csvReader);
                } catch (Exception e) {
                    handleParsingError(file, e);
                }
            }
        } catch (IOException e) {
            handleConversionError(e);
        }
        return stringWriter.toString();
    }

    private void processTrades(CSVWriter csvWriter, CSVReaderHeaderAware csvReader) throws IOException, CsvValidationException {
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            EnrichedTrade enrichedTrade = buildEnrichedTrade(line);
            if (!DateValidator.isValidDate(enrichedTrade.date())) {
                log.error("Skip trade with id {} due to invalid trade date: {}. Valid format should be yyyyMMdd", line[0], line[1]);
            } else {
                writeTrade(csvWriter, enrichedTrade);
            }
        }
    }

    private void writeTrade(CSVWriter csvWriter, EnrichedTrade trade) {
        try {
            csvWriter.writeNext(new String[]{
                    trade.date(),
                    trade.productName(),
                    trade.currency(),
                    String.valueOf(trade.price())
            }, false);
        } catch (NumberFormatException e) {
            log.error("Error parsing price for trade {}: {}", trade, e.getMessage());
        }
    }

    private void writeCsvHeader(CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{"date", "product_name", "currency", "price"}, false);
    }

    private void handleParsingError(MultipartFile file, Exception e) {
        log.error(String.format("Error parsing trade CSV file: %s", file.getName()), e);
        throw new RuntimeException(e);
    }

    private void handleConversionError(IOException e) {
        log.error("Error converting trades to CSV", e);
        throw new RuntimeException(e);
    }

    private record EnrichedTrade(String date, String productName, String currency, double price) {
    }

    private EnrichedTrade buildEnrichedTrade(String[] line) {
        String productName = productService.getProductName(Long.parseLong(line[1]));
        return new EnrichedTrade(line[0], productName, line[2], Double.parseDouble(line[3]));
    }
}
