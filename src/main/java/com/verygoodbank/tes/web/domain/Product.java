package com.verygoodbank.tes.web.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Product {
    long productId;
    String productName;
}
