package com.givemetreat.product.domain;

import java.time.LocalDateTime;

import com.givemetreat.common.generic.VOforIndexing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "[Admin] adminProductVO")
@NoArgsConstructor
@Data
public class AdminProductVO implements VOforIndexing {
	public AdminProductVO(Product product){
		this.id = product.getId();
		this.name = product.getName();
		this.category = product.getCategory().getTypeE();
		this.price = product.getPrice();
		this.agePetProper = product.getAgePetProper().getAgePetE();
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

	//AdminProductBO에서 productId를 통해 조회한 총 재고와 가용 수량
	//나중에 AdminProductBO가 두 field를 채워준다;
	private int countTotal; 
	private int countAvailable;
	
	private String name;
	private String category;
	private int price;
	private String agePetProper;
	private String imgProfile;
	private String imgThumbnail;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;	
	

}
