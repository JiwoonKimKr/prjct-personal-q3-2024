package com.givemetreat.product.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.givemetreat.common.generic.Page;
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
	public Page<ProductVO> getProductsForPaging(Integer id
										, String name
										, String category
										, Integer price
										, String agePetProper
										, String direction
										, Integer idRequested) {
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
		
		Integer index = null;
		//아무런 요청값이 없는, 메인 페이지를 띄울 때
			//요청 id가 없고, 방향도 없는 경우; 요청 id는 가장 큰 id값(첫 번째)으로, 방향은 prev로
		if(ObjectUtils.isEmpty(idRequested)) {
			idRequested = listVOs.get(0).getId();
		}
		if(ObjectUtils.isEmpty(direction)) {
			direction = "prev";
		}
		
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
		
		return pageInfo;	
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
