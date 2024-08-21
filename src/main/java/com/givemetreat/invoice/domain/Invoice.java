package com.givemetreat.invoice.domain;

import java.time.LocalDateTime;
import lombok.Data;

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
	private int hasCanceled;
	private String buyerName;
	private String buyerPhoneNumber;
	private String statusDelivery;
	private String receiverName;
	private String receiverPhoneNumber;
	private String address;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
