package com.givemetreat.product.domain;

import java.time.LocalDateTime;

import com.givemetreat.pet.domain.AgePet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Integer productInvoiceId는 ProductInvoiceBO에서,
 * Integer quantityAvailable의 경우, ProductBufferBO에서 별도로 받아와야!
 * 
 * @param product
 */
@Schema(description = "상품 관련 정보 조회 VO")
@NoArgsConstructor
@Data
public class ProductVO {

	public ProductVO (Product product) {
		this.id = product.getId();
		this.name = product.getName();
		
		this.categoryEnumerable = product.getCategory();
		this.category = product.getCategory().getTypeE();
		this.categoryTranslatedK = product.getCategory().getTypeK();
		
		this.price = product.getPrice();
		
		this.agePetEnumerable = product.getAgePetProper();
		this.agePetProper = product.getAgePetProper().getAgePetE();
		this.agePetProperTranslatedK = product.getAgePetProper().getAgePetK();
		
		this.imgProfile = product.getImgProfile();
		this.imgThumbnail = product.getImgThumbnail();
	};
	private Integer productInvoiceId;
	private Integer quantityAvailable;
	
	private Integer id;
	private String name;
	
	/*
	 * 제품 카데고리 관련
	 * kibble : 사료
	 * treat : 간식
	 */
	private CategoryProduct categoryEnumerable;
	private String category;
	private String categoryTranslatedK;
	
	private int price;
	
	/*
	 * 반려견 연령대 관련
	 * under6months : 6개월 미만
	 * adult : 성견
	 * senior : 고령견
	 */	
	private AgePet agePetEnumerable;
	private String agePetProper;
	private String agePetProperTranslatedK;

	private String imgProfile;
	private String imgThumbnail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
