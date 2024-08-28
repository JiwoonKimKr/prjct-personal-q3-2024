package com.givemetreat.invoice.domain;

import org.springframework.util.ObjectUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum StatusDelivery {
	PaymentBilled("PaymentBilled", "결제 완료", 0)
	, PackingFinished("PackingFinished", "배송준비 완료", 1)
	, TerminalHub("TerminalHub", "터미널 이동", 2)
	, EndPoint("EndPoint", "배송 상차", 3)
	, DeliveryCurrent("DeliveryCurrent", "배송중", 4)
	, DeliveryFinished("DeliveryFinished", "배송 완료", 5);
	
	
	private String statusE;
	private String statusK;
	private Integer index;
	
	private StatusDelivery(String statusE, String statusK, Integer index ) {
		this.statusE = statusE;
		this.statusK = statusK;
		this.index = index;
	}
	
	public static StatusDelivery findStatusDelivery(String statusE, String statusK, Integer index) {
		
		StatusDelivery[] arrStatus = StatusDelivery.values();
		for(StatusDelivery status : arrStatus) {
			if(ObjectUtils.isEmpty(statusE) == false && statusE.equals(status.getStatusE())) {
				return status;
			}
			if(ObjectUtils.isEmpty(statusK) == false && statusK.equals(status.getStatusK())) {
				return status;
			}
			if(ObjectUtils.isEmpty(index) == false && index == status.getIndex()) {
				return status;
			}
		}
		log.warn("[StatusDelivery findStatusDelivery()] Enum value of StatusDelivery not found."
				+ " statusE:{}, statusK:{}, index:{}", statusE, statusK, index);
		return null;
	}
}
