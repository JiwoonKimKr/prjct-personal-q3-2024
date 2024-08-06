package com.givemetreat.productInvoice.domain;

import com.givemetreat.common.utils.StringTranslator;
import com.givemetreat.product.domain.Product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
public class AdminProductInvoiceVO {

	public AdminProductInvoiceVO(Product product, Integer number) {
		this.countOrdered = number;
		this.productId = product.getId();
		this.productName = product.getName();
		this.category = StringTranslator.translateCategory(product.getCategory());
		this.price = product.getPrice();
		this.agePetProper = StringTranslator.translateAgePetProper(product.getAgePetProper());
		this.imgProfile = product.getImgProfile();
		this.imgThumbnail = product.getImgThumbnail();
	}
	
	private int countOrdered;
	private int productId;
	private String productName;
	private String category;
	private int price;
	private String agePetProper;
	private String imgProfile;
	private String imgThumbnail;
}
