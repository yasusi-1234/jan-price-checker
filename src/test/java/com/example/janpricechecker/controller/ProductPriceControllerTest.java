package com.example.janpricechecker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProductPriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void returnsProductAndPricesSortedByPriceDesc() throws Exception {
        jdbcTemplate.update("INSERT INTO products (id, jan_code, name, created_at, updated_at) VALUES (1, '4901234567894', 'Sample Green Tea 500ml', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.update("INSERT INTO shops (id, name, created_at, updated_at) VALUES (1, 'Tokyo Recycle Mart', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.update("INSERT INTO shops (id, name, created_at, updated_at) VALUES (2, 'Osaka Gadget Buyback', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.update("INSERT INTO purchase_prices (id, product_id, shop_id, price_yen, fetched_at, created_at, updated_at) VALUES (1, 1, 1, 130, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.update("INSERT INTO purchase_prices (id, product_id, shop_id, price_yen, fetched_at, created_at, updated_at) VALUES (2, 1, 2, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");

        mockMvc.perform(get("/products/4901234567894/prices"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.janCode").value("4901234567894"))
            .andExpect(jsonPath("$.productName").value("Sample Green Tea 500ml"))
            .andExpect(jsonPath("$.prices[0].shopName").value("Tokyo Recycle Mart"))
            .andExpect(jsonPath("$.prices[0].priceYen").value(130))
            .andExpect(jsonPath("$.prices[1].shopName").value("Osaka Gadget Buyback"))
            .andExpect(jsonPath("$.prices[1].priceYen").value(100));
    }

    @Test
    void returnsEmptyPricesWhenNoPriceInfoExists() throws Exception {
        jdbcTemplate.update("INSERT INTO products (id, jan_code, name, created_at, updated_at) VALUES (10, '4901234567000', 'No Price Product', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");

        mockMvc.perform(get("/products/4901234567000/prices"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.janCode").value("4901234567000"))
            .andExpect(jsonPath("$.prices").isArray())
            .andExpect(jsonPath("$.prices").isEmpty());
    }

    @Test
    void returns404WhenJanCodeNotFound() throws Exception {
        mockMvc.perform(get("/products/4901234567999/prices"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404));
    }
}
