package com.example.janpricechecker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.janpricechecker.dto.ProductPricesResponse;
import com.example.janpricechecker.service.ProductPriceService;

import jakarta.validation.constraints.Pattern;

@RestController
@RequestMapping("/products")
@Validated
public class ProductPriceController {

    private final ProductPriceService productPriceService;

    public ProductPriceController(ProductPriceService productPriceService) {
        this.productPriceService = productPriceService;
    }

    @GetMapping("/{janCode}/prices")
    public ResponseEntity<ProductPricesResponse> getProductPrices(
        @PathVariable @Pattern(regexp = "^[0-9]{8}([0-9]{5})?$") String janCode
    ) {
        ProductPricesResponse response = productPriceService.getPricesByJanCode(janCode);
        return ResponseEntity.ok(response);
    }
}
