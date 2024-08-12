package com.givemetreat.productShoppingCart.domain;


import com.givemetreat.product.domain.ProductVO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProductShoppingCartVO {
	
	public ProductShoppingCartVO (ProductShoppingCartEntity record
								, ProductVO product) {
		this.cartItemId = record.getId();
		this.productId = record.getProductId();
		this.quantity = record.getQuantity();
		
		this.productName = product.getName();
		this.price = product.getPrice();
		this.category = product.getCategory();
		this.categoryTranslated = product.getCategoryTranslatedK();
		this.agePetProper = product.getAgePetProper();
		this.agePetProperTranslated = product.getAgePetProperTranslatedK();
		this.imgProfile = product.getImgProfile();
		this.imgThumbnail = product.getImgThumbnail();		
	}
	
	public ProductShoppingCartVO (Integer cartItemId
								, ProductVO product
								, int quantity) {
		this.cartItemId = cartItemId;
		
		this.productId = product.getId();
		
		this.quantity = quantity;
		
		this.productName = product.getName();
		this.price = product.getPrice();
		this.category = product.getCategory();
		this.categoryTranslated = product.getCategoryTranslatedK();
		this.agePetProper = product.getAgePetProper();
		this.agePetProperTranslated = product.getAgePetProperTranslatedK();
		this.imgProfile = product.getImgProfile();
		this.imgThumbnail = product.getImgThumbnail();		
	}	
	
	private Integer cartItemId;
	private int productId;
	private int quantity;
	
	private String productName;
	private int price;
	private String category;
	private String categoryTranslated;
	private String agePetProper;
	private String agePetProperTranslated;
	private String imgProfile;
	private String imgThumbnail;
	

}
