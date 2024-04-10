package com.verygoodbank.tes.web.util;

import com.opencsv.CSVWriter;
import com.verygoodbank.tes.web.domain.Trade;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;
import java.util.List;

@Slf4j
@UtilityClass
public class CSVConverter {
    public static String convertToCsv(List<Trade> trades) {
        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new com.opencsv.CSVWriter(stringWriter)) {
            csvWriter.writeNext(new String[]{"date", "product_name", "currency", "price"}, false);

            for (Trade trade : trades) {
                if (!trade.isSkip()) {
                    csvWriter.writeNext(new String[]{trade.getDate(), trade.getProductName(), trade.getCurrency(), String.valueOf(trade.getPrice())}, false);
                }
            }

        } catch (Exception e) {
            log.error("Error converting trades to CSV", e);
        }

        return stringWriter.toString();
    }
}
