package com.givemetreat.productInvoice.domain;

import com.givemetreat.common.utils.StringTranslator;
import com.givemetreat.product.domain.AdminProductVO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
public class AdminProductInvoiceVO {

	public AdminProductInvoiceVO(AdminProductVO productVO, Integer number) {
		this.countOrdered = number;
		this.productId = productVO.getId();
		this.productName = productVO.getName();
		this.productBuffer = productVO.getBuffer();
		this.category = StringTranslator.translateCategory(productVO.getCategory());
		this.price = productVO.getPrice();
		this.agePetProper = StringTranslator.translateAgePetProper(productVO.getAgePetProper());
		this.imgProfile = productVO.getImgProfile();
		this.imgThumbnail = productVO.getImgThumbnail();
	}
	
	private int countOrdered;
	private int productId;
	private String productName;
	private int productBuffer;
	private String category;
	private int price;
	private String agePetProper;
	private String imgProfile;
	private String imgThumbnail;
}
