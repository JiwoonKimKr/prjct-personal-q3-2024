package com.givemetreat.invoice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ItemOrderedDto {

	@JsonProperty(value = "cartItemId", required = false)
	private Integer cartItemId;
	
	@JsonProperty("productId")
	private Integer productId; 
	
	private Integer quantity;
	
	private Integer price;
	
	private Boolean checked; 
	
}
