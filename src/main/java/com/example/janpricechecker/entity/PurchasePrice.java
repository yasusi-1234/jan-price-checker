package com.example.janpricechecker.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchase_prices")
public class PurchasePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "price_yen", nullable = false)
    private Integer priceYen;

    @Column(name = "fetched_at", nullable = false)
    private OffsetDateTime fetchedAt;

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Shop getShop() {
        return shop;
    }

    public Integer getPriceYen() {
        return priceYen;
    }

    public OffsetDateTime getFetchedAt() {
        return fetchedAt;
    }
}
