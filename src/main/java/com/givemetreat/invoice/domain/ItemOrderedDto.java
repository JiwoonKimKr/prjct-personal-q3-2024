package com.givemetreat.invoice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemOrderedDto {
	private int providerId; 

	@JsonProperty(value = "cartItemId", required = false)
	private Integer cartItemId;
	
	@JsonProperty("productId")
	private Integer productId; 
	
	private Integer quantity;
	
	private Integer price;
	
	private String checked; 
	
}
