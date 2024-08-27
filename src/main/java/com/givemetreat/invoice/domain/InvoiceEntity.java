package com.givemetreat.invoice.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.givemetreat.common.converter.HasCanceledConverter;
import com.givemetreat.common.converter.StatusDeliveryConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
	
	/* 포트원 도입하면서 해당 Column 삭제_21 08 2024
	@Column(name="paymentType")
	private String paymentType;
	private String company;
	
	@Column(name="monthlyInstallment")
	private String monthlyInstallment;
	*/
	/**
	 *  {@link HasCanceled} Enum 타입 활용
	 */
	@Convert(converter = HasCanceledConverter.class)
	@Column(name="hasCanceled")
	private HasCanceled hasCanceled;
	
	@Column(name="buyerName")
	private String buyerName;
	
	@Column(name="buyerPhoneNumber")
	private String buyerPhoneNumber;
	
	/**
	 *  {@link StatusDelivery} Enum 타입 활용
	 */
	@Convert(converter = StatusDeliveryConverter.class)
	@Column(name="statusDelivery")
	private StatusDelivery statusDelivery;
	
	@Column(name="receiverName")
	private String receiverName;
	
	@Column(name="receiverPhoneNumber")
	private String receiverPhoneNumber;
	
	/*★★★★★ 포트원 요구에 맞게
	 * addressLine1 
	 * addressLine2
	 * 그리고 난수로 생성한 paymentId가 별도로 있어야!
	*/
	private String address;
	
	@CreationTimestamp
	@Column(name="createdAt")
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name="updatedAt")
	private LocalDateTime updatedAt;
}
