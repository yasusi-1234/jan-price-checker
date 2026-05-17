package com.example.janpricechecker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.janpricechecker.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByJanCode(String janCode);
}
