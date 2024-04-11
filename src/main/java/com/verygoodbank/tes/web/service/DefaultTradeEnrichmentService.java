package com.verygoodbank.tes.web.service;

import com.verygoodbank.tes.web.domain.ProductService;
import com.verygoodbank.tes.web.domain.Trade;
import com.verygoodbank.tes.web.domain.TradeEnrichmentService;
import com.verygoodbank.tes.web.util.DateValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class DefaultTradeEnrichmentService implements TradeEnrichmentService {

    private final ProductService productService;

    public List<Trade> enrichTrades(List<Trade> trades) {
        log.info("Started enriching trades...");
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<Callable<List<Trade>>> tasks = new ArrayList<>();

        // Divide the trades into equal parts for parallel processing
        int chunkSize = (int) Math.ceil((double) trades.size() / numThreads);
        for (int i = 0; i < trades.size(); i += chunkSize) {
            int endIndex = Math.min(i + chunkSize, trades.size());
            final List<Trade> subList = trades.subList(i, endIndex);
            tasks.add(() -> enrichTradeChunk(subList));
        }

        try {
            List<Future<List<Trade>>> futures = executorService.invokeAll(tasks);
            List<Trade> enrichedTrades = new ArrayList<>();
            for (Future<List<Trade>> future : futures) {
                enrichedTrades.addAll(future.get());
            }
            return enrichedTrades;
        } catch (Exception e) {
            log.error("Error occurred while enriching trades", e);
        } finally {
            executorService.shutdown();
        }

        return trades;
    }

    private List<Trade> enrichTradeChunk(List<Trade> trades) {
        // prioritize speed and memory thriftiness, hence avoiding the use of the stream API here for filtration and processing.
        for (Trade trade : trades) {
            if (!DateValidator.isValidDate(trade.getDate())) {
                log.error("Mark trade as skipped {} due to invalid trade date: {}. Valid format should be yyyyMMdd", trade.getDate(), trade);
                trade.setSkip(true);
                continue;
            }
            long productId = trade.getProductId();
            String productName = productService.getProductName(productId);
            trade.setProductName(productName);
        }
        return trades;
    }
}
