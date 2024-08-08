package com.givemetreat.product.domain;

import java.time.LocalDateTime;

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
		this.category = category;
		this.price = price;
		this.agePetProper = agePetProper;
		this.imgProfile = imgProfile;
		this.imgThumbnail = imgThumbnail;
	};
	
	private Integer id;
	private String name;
	private String category;
	private int price;
	private String agePetProper;
	private String imgProfile;
	private String imgThumbnail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;	
}
