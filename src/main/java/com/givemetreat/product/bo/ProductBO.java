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
import com.givemetreat.productUserInterested.bo.ProductUserInterestedBO;
import com.givemetreat.productUserInterested.domain.ProductUserInterestedEntity;
import com.givemetreat.userFavorite.bo.UserFavoriteBO;
import com.givemetreat.userFavorite.domain.UserFavoriteEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductBO {
	private final ProductMapper productMapper;
	private final UserFavoriteBO userFavoriteBO;
	private final ProductUserInterestedBO productUserInterestedBO;
	
	private final int LIMIT_SELECTION = 3;
	private final int LIMIT_SELECTION_RECOMMNEDATION = 4;

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
										, Integer idRequested
										, Integer pageCurrent
										, Integer pageRequested) {
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
		if(ObjectUtils.isEmpty(idRequested)
			&& ObjectUtils.isEmpty(direction)
			&& ObjectUtils.isEmpty(pageCurrent)
			&& ObjectUtils.isEmpty(pageRequested)){
			index = 0;
			idRequested = listVOs.get(0).getId();
			direction = "prev";
		}
		
		if(ObjectUtils.isEmpty(idRequested)
			&& ObjectUtils.isEmpty(direction)
			&& ObjectUtils.isEmpty(pageCurrent) == false
			&& ObjectUtils.isEmpty(pageRequested) == false){
			direction = (pageRequested - pageCurrent) <= 0 ? "prev" : "next";
			if(direction.equals("prev")) {
				index = (pageRequested - 1) * LIMIT_SELECTION;
				index = index > 0 ? index : 0;
			} else if(direction.equals("next")) {
				index = pageRequested * LIMIT_SELECTION - 1;
				index = index <= listVOs.size() - 1 ? index : listVOs.size() - 1; 
			}
			idRequested = listVOs.get(index).getId();
		}
		
		//index 아직 입력 안 된 경우 반복문으로 찾기
			//★★★★★ 나중에 이진트리 방식으로 찾는 것도 도입하면 좋을 듯?
		if(index == null) {
			for(int i = 0; i < listVOs.size(); i++) {
				if(listVOs.get(i).getId() == idRequested) {
					index = i;
					break;
				}
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

	/**
	 * 상품 추천 관련 API
	 * @param userId
	 * @param category
	 * @param agePetProper
	 * @return
	 */
	@Transactional
	public List<ProductVO> getListProductsRecommended(Integer userId, String category, String agePetProper) {
		
		//Keyword 관련한 것을 넣어도 좋을 듯;
		String categoryFavored = category;
		String agePetProperFavored = agePetProper;
		
		UserFavoriteEntity userInfo = userFavoriteBO.getEntityByUserId(userId);
		if(ObjectUtils.isEmpty(userInfo) == false) {
			if(ObjectUtils.isEmpty(userInfo.getCategory()) == false) {
				categoryFavored = userInfo.getCategory();
			}
			if(ObjectUtils.isEmpty(userInfo.getAgePetProper()) == false) {
				agePetProperFavored = userInfo.getAgePetProper();
			}
		}
		
		ProductUserInterestedEntity itemViewed =productUserInterestedBO.getListProductsFavoredLatest(userId);
		if(ObjectUtils.isEmpty(itemViewed) == false) {
			int productId = itemViewed.getProductId();
			
			ProductVO product = getProducts(productId, null, null, null, null).get(0);
			if(ObjectUtils.isEmpty(null) == false) {
				categoryFavored = product.getCategory();
				agePetProperFavored = product.getAgePetProper();
			}
		}
		
		if(ObjectUtils.isEmpty(userInfo) && ObjectUtils.isEmpty(agePetProperFavored)) {
			return null;
		}
		
		List<Product> listProduct = productMapper.selectTop4ProductsRecommended(categoryFavored, agePetProperFavored, LIMIT_SELECTION_RECOMMNEDATION);
				
		List<ProductVO> listProductsRecommended = listProduct.stream()
													.map(product -> new ProductVO(product))
													.collect(Collectors.toList());
		
		return listProductsRecommended;
	}
}
