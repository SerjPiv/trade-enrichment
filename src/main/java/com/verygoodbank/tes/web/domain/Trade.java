package com.verygoodbank.tes.web.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Trade {
    String date;
    long productId;
    String currency;
    double price;
    String productName;
    boolean skip;
}
