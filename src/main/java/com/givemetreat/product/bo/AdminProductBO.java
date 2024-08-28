package com.givemetreat.product.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.givemetreat.common.FileManagerService;
import com.givemetreat.common.generic.Page;
import com.givemetreat.pet.domain.AgePet;
import com.givemetreat.product.domain.AdminProductVO;
import com.givemetreat.product.domain.CategoryProduct;
import com.givemetreat.product.domain.Product;
import com.givemetreat.product.mapper.ProductMapper;
import com.givemetreat.productBuffer.bo.ProductBufferBO;
import com.givemetreat.productBuffer.domain.ProductBufferEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminProductBO {
	private final ProductMapper productMapper;
	private final ProductBufferBO productBufferBO;

	private final int LIMIT_SELECTION = 3;

	/**
	 * AdminProductBO에서 paging 형태로 Controller에 넘기게 될 메소드 (●'◡'●)
	 * @param id
	 * @param name
	 * @param category
	 * @param price
	 * @param agePetProper
	 * @param direction
	 * @param idRequested
	 * @param pageCurrent
	 * @param pageRequested
	 * @return Page<{@link AdminProductVO}>
	 */
	@Transactional
	public Page<AdminProductVO> getProductsForPaging(Integer id
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
		
		// 출발 금액이 상한 금액 보다 클 때 맞바꾸도록
		if(ObjectUtils.isEmpty(priceFrom) == false 
				&& ObjectUtils.isEmpty(priceUntil) == false) {
			priceFrom = priceUntil > priceFrom ? priceFrom : priceUntil;
			priceUntil = priceFrom < priceUntil ? priceUntil : priceFrom;
			log.info("[AdminProductBO getProductsForPaging()]"
					+ " priceFrom & priceUntil get swapped. priceFrom:{}, priceUntil:{}", priceFrom, priceUntil);
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
		List<AdminProductVO> listVOs = new ArrayList<>();
		for(Product product : listProductsWhole) {
			AdminProductVO vo = new AdminProductVO(product);
			Integer productId = product.getId();
			
			//총 재고조회_배송예정 수량 포함
			vo.setCountTotal(productBufferBO.getTotalCountByProductId(productId));
			//가용 재고_배송예정 수량 제외
			vo.setCountAvailable(productBufferBO.getCountAvailableByProductIdAndReserved(productId, false));
			listVOs.add(vo);
		}
		
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
		
		Page<AdminProductVO> pageInfo = new Page<AdminProductVO>(listVOs
														, listVOs.get(0).getId()
														, listVOs.get(listVOs.size() - 1).getId()
														, direction
														, index
														, idRequested
														, LIMIT_SELECTION);
		
		log.info("[ProductBO getProductsForPaging()] new Page<ProductVO>:", pageInfo);
		
		return pageInfo;	
	}	
	
	
	public List<AdminProductVO> getProduct(Integer id
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
		//Product를 VO로 변환 후 List 형태로 반환
		List<AdminProductVO> listVOs = new ArrayList<>();
		for(Product product : listProducts) {
			AdminProductVO vo = new AdminProductVO(product);
			Integer productId = product.getId();
			
			//총 재고조회_배송예정 수량 포함
			vo.setCountTotal(productBufferBO.getTotalCountByProductId(productId));
			//가용 재고_배송예정 수량 제외
			vo.setCountAvailable(productBufferBO.getCountAvailableByProductIdAndReserved(productId, false));
			listVOs.add(vo);
		}
		return listVOs;
	}
	
	@Transactional
	public int registerProduct(String name
							, String category
							, int price
							, String agePetProper,
							MultipartFile imageProduct
							, int quantity) {

		List<String> ListpathsImageProfile = FileManagerService
												.uploadImageWithThumbnail(
														imageProduct
														, "productProfile");
		
		Product productToInsert = new Product(name
										, category
										, price
										, agePetProper
										, ListpathsImageProfile.get(0) //imgProfile
										, ListpathsImageProfile.get(1)); //imgThumbnail
		
		
		
		// MyBatis에서 생성된 record의 ID값을 얻으려는데, 계속 에러 발생 ㅠ
		//아예 Product를 생성해서 parameter로 넘기는 방식으로 선회_13:30_08 08 2024
		int countRecorded = productMapper.insertProduct(productToInsert);
		
		//Mapper를 모두 거쳐서 해당 Entity에서 ID값을 빼올 수 있다!
		int productId = productToInsert.getId();
		log.info("[AdminProductBO registerProduct()] id of Product inserted:{}", productId);
		
		//바로 count를 controller에 return하기 전에
		//productBufferBO에서 해당 quantity만큼 record들을 생성
		
		productBufferBO.addProductBuffersInQuantity(productToInsert.getId(), quantity);	
		
		log.info("[AdminProductBO] requested ProductBufferBO.addProductBuffersInQuantity(productId:{}, quantity:{})", productId, quantity);
		
		return countRecorded;
	}

	public int deleteProduct(int productId) {
		List<ProductBufferEntity> listBuffers = productBufferBO.getListProductBuffersByProductId(productId);
		if(ObjectUtils.isEmpty(listBuffers)) {
			log.warn("[AdminProductBO deleteProduct()] any product stock not found. productId:{}", productId);
			return 0;
		}
		
		productBufferBO.deleteListBuffers(listBuffers);
		log.info("[AdminProductBO deleteProduct()] requested Product stocks get deleted. productId:{}", productId);
		
		//제품 이미지 삭제
		AdminProductVO product = getProduct(productId, null, null, null, null).get(0);
		String imageProfile = product.getImgProfile();
		String imageThumbnail = product.getImgThumbnail();
		
		FileManagerService.deleteImageOriginAndThumbnail(imageProfile, imageThumbnail);
		
		return productMapper.deleteProduct(productId);
	}

}
