package com.givemetreat.product.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.givemetreat.common.generic.Page;
import com.givemetreat.common.generic.VOforIndexing;
import com.givemetreat.common.utils.IndexBinarySearchTreeUtil;
import com.givemetreat.common.utils.SearchMapFromKeyword;
import com.givemetreat.pet.domain.AgePet;
import com.givemetreat.product.domain.CategoryProduct;
import com.givemetreat.product.domain.Product;
import com.givemetreat.product.domain.ProductVO;
import com.givemetreat.product.mapper.ProductMapper;
import com.givemetreat.productBuffer.bo.ProductBufferBO;
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
	private final ProductBufferBO productBufferBO;
	
	private final UserFavoriteBO userFavoriteBO;
	private final ProductUserInterestedBO productUserInterestedBO;
	
	private final IndexBinarySearchTreeUtil indexBinarySearchTreeUtil;

	private final int LIMIT_SELECTION = 6;
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
	public Page<ProductVO> getProductsForPaging(String keyword
										, Integer id
										, String name
										, String category
										, Integer priceFrom
										, Integer priceUntil
										, String agePetProper
										, String direction
										, Integer idRequested
										, Integer pageCurrent
										, Integer pageRequested) {
		//direction이랑 paging이 필요한 경우
		
		//Enum 타입 추가_27 08 2024
		CategoryProduct categoryCurrent = CategoryProduct.findCategoryProduct(category, null, null);
		AgePet agePetCurrent = AgePet.findAgeCurrent(agePetProper, null, null);
		
		//Keyword 입력된 경우 SearchMapFromKeyword 검색어 유틸리티 활용하도록 작성_28 08 2024
		if(ObjectUtils.isEmpty(keyword) == false) {
			SearchMapFromKeyword keywordMap = new SearchMapFromKeyword(keyword);
			
			if(ObjectUtils.isEmpty(keywordMap.getProductName()) == false) {
				name = keywordMap.getProductName();
				log.info("[ProductBO getProductsForPaging()] name get updated by keywordMap. name:{}", name);
			}
			if(ObjectUtils.isEmpty(keywordMap.getCategory()) == false) {
				categoryCurrent = keywordMap.getCategory();
				log.info("[ProductBO getProductsForPaging()] CategoryProduct Enum get updated by keywordMap. category:{}", categoryCurrent);
			}
			if(ObjectUtils.isEmpty(keywordMap.getAgePetProper()) == false) {
				agePetCurrent = keywordMap.getAgePetProper();
				log.info("[ProductBO getProductsForPaging()] AgePet Enum get updated by keywordMap. agePetProper:{}", agePetCurrent);
			}
		}
		
		List<Product> listProductsWhole = productMapper.selectProductForPaging(null
																			, name
																			, categoryCurrent
																			, priceFrom
																			, priceUntil
																			, agePetCurrent
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
		if(index == null) {
			@SuppressWarnings("unchecked")
			List<VOforIndexing> list = (List<VOforIndexing>) (List<?>) listVOs;
			index = indexBinarySearchTreeUtil.findIndexFromList(list, idRequested);
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
		//Enum 타입 추가_27 08 2024
		CategoryProduct categoryCurrent = CategoryProduct.findCategoryProduct(category, null, null);
		AgePet agePetCurrent = AgePet.findAgeCurrent(agePetProper, null, null);
		List<Product> listProducts = productMapper.selectProduct(id
																, name
																, categoryCurrent
																, price
																, agePetCurrent);
		List<ProductVO> listVOs = new ArrayList<>();
		
		for(Product product : listProducts) {
			ProductVO vo = new ProductVO(product);
			Integer quantityAvailable = productBufferBO.getCountAvailableByProductIdAndReserved(product.getId(), false);
			if(ObjectUtils.isEmpty(quantityAvailable)) {
				log.warn("[getProducts] quantity Available failed to get figured out. product Id:{}", product.getId());
			}
			
			vo.setQuantityAvailable(quantityAvailable);
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
		//Enum 타입 추가_27 08 2024
		CategoryProduct categoryFavored = CategoryProduct.findCategoryProduct(category, null, null);
		AgePet agePetProperFavored = AgePet.findAgeCurrent(agePetProper, null, null);
		
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
		
		//조회한 상품 상세 페이지가 필터링 선택보다 나중에 발생한 이벤트인 경우에만
		if(ObjectUtils.isEmpty(itemViewed) == false
				&& itemViewed.getUpdatedAt().isAfter(userInfo.getUpdatedAt())) {
			int productId = itemViewed.getProductId();
			
			ProductVO product = getProducts(productId, null, null, null, null).get(0);
			
			if(ObjectUtils.isEmpty(product) == false) {
				categoryFavored = product.getCategoryEnumerable();
				agePetProperFavored = product.getAgePetEnumerable();
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
