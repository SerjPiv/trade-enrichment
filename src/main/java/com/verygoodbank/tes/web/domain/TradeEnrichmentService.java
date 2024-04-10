package com.verygoodbank.tes.web.domain;

import java.util.List;

public interface TradeEnrichmentService {
    List<Trade> enrichTrades(List<Trade> trades);
}
