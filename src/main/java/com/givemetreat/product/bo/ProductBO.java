package com.givemetreat.product.bo;

import java.util.ArrayList;
import java.util.Collections;
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

	/**
	 * 
	 * @param id
	 * @param name
	 * @param category
	 * @param price
	 * @param agePetProper
	 * @return List<{@link ProductVO}>
	 */
	@Transactional
	public List<ProductVO> getProductsForPaging(Integer id
										, String name
										, String category
										, Integer price
										, String agePetProper
										, String direction
										, Integer index) {
		
		List<Product> listProducts = productMapper.selectProductForPaging(id
																, name
																, category
																, price
																, agePetProper
																, direction
																, index);
		List<ProductVO> listVOs = new ArrayList<>();
		
		for(Product product : listProducts) {
			ProductVO vo = new ProductVO(product);
			listVOs.add(vo);
		}
		
		if(direction != null && direction.equals("prev")) {
			Collections.reverse(listVOs);
		}
		
		return listVOs;	
	}

	@Transactional
	public List<ProductVO> getProducts(Integer id
										, String name
										, String category
										, Integer price
										, String agePetProper) {
		List<Product> listProducts = productMapper.selectProduct(id
																, name
																, category
																, price
																, agePetProper);
		List<ProductVO> listVOs = new ArrayList<>();
		
		for(Product product : listProducts) {
			ProductVO vo = new ProductVO(product);
			listVOs.add(vo);
		}
		
		return listVOs;	
	}
}
