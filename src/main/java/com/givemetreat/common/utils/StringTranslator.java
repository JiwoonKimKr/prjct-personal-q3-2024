package com.givemetreat.common.utils;

public class StringTranslator {
	public static String translatePaymentType(String type) {
		/*
		 * 신용카드: CreditCard
		 * 계좌이체: AccounTranster
		 * 
		 * TODO 다른 할부 개월 목록 더 추가해야
		 */
		
		if(type.equals("CreditCard")) return "신용카드";
		if(type.equals("AccounTranster")) return "계좌이체";
		return "";
	}
	
	public static String translateMonthlyInstallment(String type) {
		/*
		 * 일시불: instance
		 * 2개월: 2M
		 * 3개월: 3M
		 * 
		 * TODO 다른 할부 개월 목록 더 추가해야
		 */
		
		if(type.equals("instance")) return "일시불";
		if(type.endsWith("M")) return type.replace("M", "개월");
		return "";
	}
	
	public static String translateHasCanceled(int type) {
		/*
		 * 0: 온전한 결제 완료
		 * 1: 결제 취소 상태
		 * 
		 * TODO 다른 할부 개월 목록 더 추가해야
		 */
		
		if(type == 0) return "결제 완료";
		if(type == 1) return "결제 취소";
		return "";
	}
	
	public static String translateStatusDelivery(String status) {
		if(status.equals("PaymentBilled")) return "결제 완료";
		if(status.equals("PackingFinished")) return "배송준비 완료";
		if(status.equals("TerminalHub")) return "터미널 이동";
		if(status.equals("EndPoint")) return "배송 상차";
		if(status.equals("DeliveryCurrent")) return "배송중";
		return "";
	}
}
