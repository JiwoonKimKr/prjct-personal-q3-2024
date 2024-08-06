package com.givemetreat.invoice.domain;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.givemetreat.common.utils.StringTranslator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Data
public class AdminInvoiceVO {
	
	public AdminInvoiceVO(InvoiceEntity invoice) {
		this.id = invoice.getId();
		this.userId = invoice.getUserId();
		this.payment = invoice.getPayment();
		
		//결제 타입: 신용카드 등등
		this.paymentType = StringTranslator.translatePaymentType(
							invoice.getPaymentType());
		
		this.company = invoice.getCompany();
		
		//할부 타입: 일시불, 2개월 할부 등등
		this.monthlyInstallment = StringTranslator.translateMonthlyInstallment(
									invoice.getMonthlyInstallment());
				
		//결제 취소 여부: 1은 결제 취소,0 은 결제완료 그대로 
		this.hasCanceled = StringTranslator.translateHasCanceled(
							invoice.getHasCanceled());
		
		this.buyerName = invoice.getBuyerName();
		this.buyerPhoneNumber = invoice.getBuyerPhoneNumber();
		this.statusDelivery = StringTranslator.translateStatusDelivery(
								invoice.getStatusDelivery());
		
		this.receiverName = invoice.getReceiverName();
		this.receiverPhoneNumber = invoice.getReceiverPhoneNumber();
		this.address = invoice.getAddress();
		this.createdAt = invoice.getCreatedAt();
		this.updatedAt = invoice.getUpdatedAt();
	}
	
	private int id;
	
	@JsonProperty(value="userId")
	private int userId;
	
	@JsonProperty(value="loginId")
	private String loginId;
	private int payment;
	
	//결제타입; 신용카드:creditCard
	@JsonProperty(value="paymentType")
	private String paymentType;
	
	private String company;
	
	@JsonProperty(value="monthlyInstallment")
	private String monthlyInstallment;
	
	@JsonProperty(value="hasCanceled")	
	private String hasCanceled;
	
	@JsonProperty(value="buyerName")
	private String buyerName;
	
	@JsonProperty(value="buyerPhoneNumber")
	private String buyerPhoneNumber;
	
	@JsonProperty(value="statusDelivery")
	private String statusDelivery;
	
	@JsonProperty(value="receiverName")
	private String receiverName;
	
	@JsonProperty(value="receiverPhoneNumber")
	private String receiverPhoneNumber;
	
	private String address;
	
	@JsonProperty(value="createdAt")
	private LocalDateTime createdAt;
	
	@JsonProperty(value="updatedAt")
	private LocalDateTime updatedAt;
	
	

	
}
