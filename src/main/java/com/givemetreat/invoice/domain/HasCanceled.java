package com.givemetreat.invoice.domain;

import org.springframework.util.ObjectUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum HasCanceled {
	billed(0, "결제 완료")
	,canceled(1, "결제 취소");
	
	private Integer index;
	private String statusK;
	
	private HasCanceled(Integer index, String statusK) {
		this.index = index;
		this.statusK = statusK;
	}
	
	public static HasCanceled findIfPaymentCanceled(Integer index, String statusK) {
		
		HasCanceled[] arrStatus = HasCanceled.values();
		for(HasCanceled status : arrStatus) {
			if(ObjectUtils.isEmpty(index) == false && index == status.getIndex()) {
				return status;
			}
			if(ObjectUtils.isEmpty(statusK) == false && statusK.equals(status.getStatusK())) {
				return status;
			}
		}
		log.warn("[HasCanceled findIfPaymentCanceled()] Enum value of HasCanceled not found."
				+ " index:{}, statusK:{}", index, statusK);
		return null;
	}
}
