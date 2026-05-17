package com.example.janpricechecker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.janpricechecker.dto.PriceDto;
import com.example.janpricechecker.dto.ProductDto;
import com.example.janpricechecker.dto.ProductPricesResponse;
import com.example.janpricechecker.exception.ProductNotFoundException;
import com.example.janpricechecker.mapper.ProductPriceMapper;

@Service
public class ProductPriceService {

    private final ProductPriceMapper productPriceMapper;

    public ProductPriceService(ProductPriceMapper productPriceMapper) {
        this.productPriceMapper = productPriceMapper;
    }

    public ProductPricesResponse getPricesByJanCode(String janCode) {
        ProductDto product = productPriceMapper.findProductByJanCode(janCode);
        if (product == null) {
            throw new ProductNotFoundException(janCode);
        }

        List<PriceDto> prices = productPriceMapper.findLatestPricesByProductId(product.id());
        return new ProductPricesResponse(product.janCode(), product.name(), prices);
    }
}
