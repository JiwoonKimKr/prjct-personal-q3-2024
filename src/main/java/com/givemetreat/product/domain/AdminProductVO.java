package com.givemetreat.product.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AdminProductVO {
	public AdminProductVO(Product product){
		this.id = product.getId();
		this.name = product.getName();
		this.category = product.getCategory();
		this.price = product.getPrice();
		this.agePetProper = product.getAgePetProper();
		this.imgProfile = product.getImgProfile();
		this.imgThumbnail = product.getImgThumbnail();
		this.createdAt = product.getCreatedAt();
		this.updatedAt = product.getUpdatedAt();
	}	
/*
	id
	name
	category
	price
	agePetProper
	imgProfile
	imgThumbnail
	createdAt
	updatedAt
 */
	private int id;
	
	private int buffer; //product_buffer DB table에서 별도로 얻어와야ㅠ
	
	private String name;
	private String category;
	private int price;
	private String agePetProper;
	private String imgProfile;
	private String imgThumbnail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;	
	

}
