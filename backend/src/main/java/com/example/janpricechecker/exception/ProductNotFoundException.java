package com.example.janpricechecker.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String janCode) {
        super("Product not found for JAN code: " + janCode);
    }
}
