package com.example.janpricechecker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.janpricechecker.dto.PriceDto;
import com.example.janpricechecker.dto.ProductDto;

@Mapper
public interface ProductPriceMapper {

    ProductDto findProductByJanCode(@Param("janCode") String janCode);

    List<PriceDto> findLatestPricesByProductId(@Param("productId") Long productId);
}
