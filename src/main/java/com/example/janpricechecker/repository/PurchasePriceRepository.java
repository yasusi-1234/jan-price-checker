package com.example.janpricechecker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.janpricechecker.entity.PurchasePrice;

public interface PurchasePriceRepository extends JpaRepository<PurchasePrice, Long> {

    List<PurchasePrice> findByProductIdOrderByPriceYenDesc(Long productId);
}
