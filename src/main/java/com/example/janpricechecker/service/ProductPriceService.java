package com.example.janpricechecker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.janpricechecker.dto.PriceDto;
import com.example.janpricechecker.dto.ProductPricesResponse;
import com.example.janpricechecker.entity.Product;
import com.example.janpricechecker.entity.PurchasePrice;
import com.example.janpricechecker.exception.ProductNotFoundException;
import com.example.janpricechecker.repository.ProductRepository;
import com.example.janpricechecker.repository.PurchasePriceRepository;

@Service
public class ProductPriceService {

    private final ProductRepository productRepository;
    private final PurchasePriceRepository purchasePriceRepository;

    public ProductPriceService(
        ProductRepository productRepository,
        PurchasePriceRepository purchasePriceRepository
    ) {
        this.productRepository = productRepository;
        this.purchasePriceRepository = purchasePriceRepository;
    }

    @Transactional(readOnly = true)
    public ProductPricesResponse getPricesByJanCode(String janCode) {
        Product product = productRepository.findByJanCode(janCode)
            .orElseThrow(() -> new ProductNotFoundException(janCode));

        List<PurchasePrice> purchasePrices = purchasePriceRepository
            .findByProductIdOrderByPriceYenDesc(product.getId());

        List<PriceDto> prices = new ArrayList<>();
        for (PurchasePrice purchasePrice : purchasePrices) {
            PriceDto priceDto = new PriceDto(
                purchasePrice.getShop().getName(),
                purchasePrice.getPriceYen(),
                purchasePrice.getFetchedAt()
            );
            prices.add(priceDto);
        }

        return new ProductPricesResponse(product.getJanCode(), product.getName(), prices);
    }
}
