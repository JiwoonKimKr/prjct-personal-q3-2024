package com.givemetreat.invoice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemOrderedDto {

	/**
	 * 장바구니 productShoppingCart PK
	 */
	@JsonProperty(value = "cartItemId", required = false)
	private Integer cartItemId;
	
	@JsonProperty("productId")
	private Integer productId; 
	
	private Integer quantity;
	
	private Integer price;
	
	private String checked; 

	/**
	 * 결제 후 해당 invoice에 연동된 productInvoiceId PK
	 */
	@JsonProperty(value = "productInvoiceId", required = false)
	private Integer productInvoiceId;
}
