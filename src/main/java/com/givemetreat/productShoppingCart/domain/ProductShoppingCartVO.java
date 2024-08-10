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
		this.category = product.getCategory();
		this.categoryTranslated = product.getCategoryTranslatedK();
		this.agePetProper = product.getAgePetProper();
		this.agePetProperTranslated = product.getAgePetProperTranslatedK();
		
	}
	
	private int cartItemId;
	private int productId;
	private int quantity;
	
	private String productName;
	private String category;
	private String categoryTranslated;
	private String agePetProper;
	private String agePetProperTranslated;
	

}
