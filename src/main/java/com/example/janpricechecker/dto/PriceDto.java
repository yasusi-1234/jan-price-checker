package com.example.janpricechecker.dto;

import java.time.OffsetDateTime;

public record PriceDto(
    String shopName,
    Integer priceYen,
    OffsetDateTime fetchedAt
) {
}
