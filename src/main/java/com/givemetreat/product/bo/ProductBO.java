package com.givemetreat.product.bo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.givemetreat.common.Page;
import com.givemetreat.product.domain.Product;
import com.givemetreat.product.domain.ProductVO;
import com.givemetreat.product.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductBO {
	private final ProductMapper productMapper;
	
	private final int LIMIT_SELECTION = 3;

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
										, Integer idRequested) {
		
		if(direction == null || idRequested == null) {
			List<Product> listProducts = productMapper.selectProductForPaging(id
																			, name
																			, category
																			, price
																			, agePetProper
																			, direction
																			, idRequested
																			, LIMIT_SELECTION);
			return listProducts.stream()
								.map(product -> new ProductVO(product))
								.collect(Collectors.toList());
		}
		
		//direction이랑 paging이 필요한 경우
		
		List<Product> listProductsWhole = productMapper.selectProductForPaging(null
																			, name
																			, category
																			, price
																			, agePetProper
																			, null
																			, null
																			, null);
		List<ProductVO> listVOs = listProductsWhole.stream()
												.map(product -> new ProductVO(product))
												.collect(Collectors.toList());
		
		if(direction.equals("prev")) {
			Collections.reverse(listVOs);
		}
		
		Integer index = null;
		for(int i = 0; i < listVOs.size(); i++) {
			if(listVOs.get(i).getId() == idRequested) {
				index = i;
				break;
			}
		}
		Page<ProductVO> pageInfo = new Page<ProductVO>(listVOs
														, listVOs.get(0).getId()
														, listVOs.get(listVOs.size() - 1).getId()
														, direction
														, index
														, idRequested
														, LIMIT_SELECTION);
		
		log.info("[ProductBO getProductsForPaging()] new Page<ProductVO>:", pageInfo);
		
		return pageInfo.returnPageList();	
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
