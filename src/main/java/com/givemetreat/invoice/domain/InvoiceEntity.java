package com.givemetreat.invoice.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Table(name = "invoice")
@Entity
public class InvoiceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="userId")
	private int userId;
	private int payment;
	
	@Column(name="paymentType")
	private String paymentType;
	private String company;
	
	@Column(name="monthlyInstallment")
	private String monthlyInstallment;
	
	@Column(name="hasCanceled")
	private int hasCanceled;
	
	@Column(name="buyerName")
	private String buyerName;
	
	@Column(name="buyerPhoneNumber")
	private String buyerPhoneNumber;
	
	@Column(name="statusDelivery")
	private String statusDelivery;
	
	@Column(name="receiverName")
	private String receiverName;
	
	@Column(name="receiverPhoneNumber")
	private String receiverPhoneNumber;
	
	private String address;
	
	@CreationTimestamp
	@Column(name="createdAt")
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name="updatedAt")
	private LocalDateTime updatedAt;
}
