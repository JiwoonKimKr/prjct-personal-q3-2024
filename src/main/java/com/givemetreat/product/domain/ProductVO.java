package com.givemetreat.product.domain;

import java.time.LocalDateTime;

import com.givemetreat.common.utils.StringTranslator;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductVO {

	public ProductVO (Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.category = product.getCategory();
		this.categoryTranslatedK = StringTranslator.translateCategoryE2K(product.getCategory());
		
		this.price = product.getPrice();
		
		this.agePetProper = product.getAgePetProper();
		this.agePetProperTranslatedK = StringTranslator.translateAgePetProperE2K(product.getAgePetProper());
		
		this.imgProfile = product.getImgProfile();
		this.imgThumbnail = product.getImgThumbnail();
	};
	private Integer productInvoiceId;
	
	private Integer id;
	private String name;
	
	/*
	 * 제품 카데고리 관련
	 * kibble : 사료
	 * treat : 간식
	 */
	private String category;
	private String categoryTranslatedK;
	
	private int price;
	
	/*
	 * 반려견 연령대 관련
	 * under6months : 6개월 미만
	 * adult : 성견
	 * senior : 고령견
	 */	
	private String agePetProper;
	private String agePetProperTranslatedK;

	private String imgProfile;
	private String imgThumbnail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
