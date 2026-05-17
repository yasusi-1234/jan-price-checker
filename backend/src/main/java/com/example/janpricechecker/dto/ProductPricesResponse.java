package com.example.janpricechecker.dto;

import java.util.List;

public record ProductPricesResponse(
    String janCode,
    String productName,
    List<PriceDto> prices
) {
}
