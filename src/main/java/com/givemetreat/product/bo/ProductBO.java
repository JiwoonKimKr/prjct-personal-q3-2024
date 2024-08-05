package com.givemetreat.product.bo;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.givemetreat.product.domain.Product;
import com.givemetreat.product.mapper.ProductMapper;

@RequiredArgsConstructor
@Service
public class ProductBO {
	private final ProductMapper productMapper;

	public List<Product> getProduct(Integer id
								, String name
								, String category
								, Integer price
								, String agePetProper) {
		return productMapper.selectProduct(id, name, category, price, agePetProper);
	}

}
