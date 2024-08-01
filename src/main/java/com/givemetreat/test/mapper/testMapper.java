package com.givemetreat.test.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.givemetreat.product.domain.Product;

@Mapper
public interface testMapper {

	public Product getTestFirstDummy();

}
