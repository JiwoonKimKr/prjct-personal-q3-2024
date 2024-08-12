package com.givemetreat.invoice.domain;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class InvoiceDto {
	
	@JsonProperty("listItemOrdered")
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
