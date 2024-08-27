package com.givemetreat.product.domain;

import java.time.LocalDateTime;

import com.givemetreat.pet.domain.AgePet;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Product {
	
	public Product(
			String name
			, String category
			, int price
			, String agePetProper
			, String imgProfile
			, String imgThumbnail) {
		this.name = name;
		this.category = CategoryProduct.findCategoryProduct(category, null, null) ;
		this.price = price;
		this.agePetProper = AgePet.findAgeCurrent(agePetProper, null, null);
		this.imgProfile = imgProfile;
		this.imgThumbnail = imgThumbnail;
	};
	
	private Integer id;
	private String name;
	
	/**
	 *  {@link CategoryProduct} Enum 타입 활용
	 */
	private CategoryProduct category;
	private int price;
	
	/**
	 *  {@link AgePet} Enum 타입 활용
	 */
	private AgePet agePetProper;
	private String imgProfile;
	private String imgThumbnail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;	
}
