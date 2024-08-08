package com.givemetreat.product.bo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.givemetreat.product.domain.Product;
import com.givemetreat.product.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductBO {
	private final ProductMapper productMapper;

	@Transactional
	public List<Product> getProduct(Integer id
										, String name
										, String category
										, Integer price
										, String agePetProper) {
	 return productMapper.selectProduct(id, name, category, price, agePetProper);	
	}
}
