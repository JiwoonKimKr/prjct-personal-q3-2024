package com.givemetreat.productInvoice.domain;

import com.givemetreat.common.utils.StringTranslator;
import com.givemetreat.product.domain.ProductVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemOrderedVO {

	public ItemOrderedVO(ProductVO product, Integer quantity) {
		this.productInvoiceId = product.getProductInvoiceId();
		this.quantity = quantity;
		this.productId = product.getId();
		this.productName = product.getName();
		
		this.category = StringTranslator.translateCategoryE2K(product.getCategory());
		this.price = product.getPrice();
		this.agePetProper = StringTranslator.translateAgePetProperE2K(product.getAgePetProper());
		this.imgProfile = product.getImgProfile();
		this.imgThumbnail = product.getImgThumbnail();
	}

	private Integer productInvoiceId;
	
	private Integer productId;
	private String productName;
	private String category;
	private Integer price;
	private String agePetProper;
	private String imgProfile;
	private String imgThumbnail;
	
	private Integer quantity;
}
