package com.givemetreat.invoice.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class Invoice {
	private int id;
	private int userId;
	private int payment;
	/*
	private String paymentType;
	private String company;
	private String monthlyInstallment;
	*/
	
	
	private HasCanceled hasCanceled;

	public void setHasCanceled(Integer index) {
		if(index == 0) {
			this.hasCanceled = HasCanceled.billed;
		}
		if(index == 1) {
			this.hasCanceled = HasCanceled.canceled;
		}
	}
	
	private String buyerName;
	private String buyerPhoneNumber;
	/**
	 *  {@link StatusDelivery} Enum 타입 활용
	 */
	private StatusDelivery statusDelivery;
	private String receiverName;
	private String receiverPhoneNumber;
	private String address;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
