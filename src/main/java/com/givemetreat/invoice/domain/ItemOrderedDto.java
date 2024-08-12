package com.givemetreat.invoice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(value = PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class ItemOrderedDto {

	@JsonProperty(value = "cartItemId", required = false)
	private Integer cartItemId;
	
	@JsonProperty("productId")
	private Integer productId; 
	
	private Integer quantity;
	
	private Integer price;
	
	private Boolean checked; 
	
}
