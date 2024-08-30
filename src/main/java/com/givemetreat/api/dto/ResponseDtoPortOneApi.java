package com.givemetreat.api.dto;

import com.givemetreat.invoice.domain.InvoiceEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "[Client] ResponseDtoPortOneApi; 포트원 결제응답 class")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDtoPortOneApi {
	
	/**
	 * 포트원 결제응답 객체 생성자
	 * @param storeId
	 * @param channelKey
	 * @param invoice
	 * @param loginId
	 */
	public ResponseDtoPortOneApi(String storeId
								, String channelKey 
								, InvoiceEntity invoice
								, String loginId) {
		this.storeId = storeId;
		this.channelKey = channelKey;
		this.paymentId = String.format("givemetreat-%s",invoice.getId());
		this.orderName = String.format("orderName-%s-%s", invoice.getId(), invoice.getCreatedAt());
		this.totalAmount = invoice.getPayment();
		this.fullName = invoice.getBuyerName();
		this.email = loginId;
		this.phoneNumber = invoice.getBuyerPhoneNumber();
		this.invoiceId = invoice.getId();
		
		String[] listStrAddress = invoice.getAddress().split(" ");
		String addressLine1 = "".concat(listStrAddress[0]).concat(" ").concat(listStrAddress[1]);
		String addressLine2 = "";
		for(int i = 2; i < listStrAddress.length; i++ ) {
			addressLine2 = addressLine2.concat(listStrAddress[i]).concat(" ");
		}
		
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
	}
	

	private String storeId;
	private String channelKey;
	private String paymentId;
	private String orderName;
	private Integer totalAmount;
	private String fullName;
	private String email;
	private String phoneNumber;
	private Integer invoiceId;
	
	private String addressLine1;
	private String addressLine2;
}
