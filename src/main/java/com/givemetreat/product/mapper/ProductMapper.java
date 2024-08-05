package com.givemetreat.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.givemetreat.product.domain.Product;

@Mapper
public interface ProductMapper {

	public List<Product> selectProduct(
			@Param("id") Integer id
			, @Param("name") String name
			, @Param("category") String category
			, @Param("price") Integer price
			, @Param("agePetProper") String agePetProper);
	
}
