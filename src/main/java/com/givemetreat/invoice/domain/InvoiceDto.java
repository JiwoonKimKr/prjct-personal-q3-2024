package com.givemetreat.invoice.domain;

import java.util.List;

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
public class InvoiceDto {

	@JsonProperty(value = "listItemOrdered")
	private List<ItemOrderedDto> listItemOrdered;

	private int payment;
	
	@JsonProperty("paymentType")
	private String paymentType;
	
	private String company;
	
	@JsonProperty("monthlyInstallment")
	private String monthlyInstallment;
	
	@JsonProperty("buyerName")
	private String buyerName;
	
	@JsonProperty("buyerPhoneNumber")
	private String buyerPhoneNumber;
	
	@JsonProperty("receiverName")
	private String receiverName;
	
	@JsonProperty("receiverPhoneNumber")
	private String receiverPhoneNumber;
	
	private String address	;
}
