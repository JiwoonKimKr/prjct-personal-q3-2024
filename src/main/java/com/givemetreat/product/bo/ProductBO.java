package com.givemetreat.product.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.givemetreat.product.domain.Product;
import com.givemetreat.product.domain.ProductVO;
import com.givemetreat.product.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductBO {
	private final ProductMapper productMapper;

	@Transactional
	public List<ProductVO> getProduct(Integer id
										, String name
										, String category
										, Integer price
										, String agePetProper) {
		
		List<Product> listProducts = productMapper.selectProduct(id, name, category, price, agePetProper);
		List<ProductVO> listVOs = new ArrayList<>();
		
		for(Product product : listProducts) {
			ProductVO vo = new ProductVO(product);
			listVOs.add(vo);
		}
		
		return listVOs;	
	}
}
