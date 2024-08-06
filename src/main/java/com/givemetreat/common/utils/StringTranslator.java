package com.givemetreat.common.utils;

public class StringTranslator {
	/* 제품 상세 내용 관련 */
	
	/*
	 * 제품 카데고리 관련
	 * kibble : 사료
	 * treat : 간식
	 */
	public static String translateCategory(String type) {
		if(type.equals("kibble")) return "사료";
		if(type.equals("treat")) return "간식";

		return "";
	}
	
	/*
	 * 반려견 연령대 관련
	 * under6months : 6개월 미만
	 * adult : 성견
	 * senior : 고령견
	 */
	public static String translateAgePetProper(String type) {
		if(type.equals("under6months")) return "6개월 미만";
		if(type.equals("adult")) return "성견";
		if(type.equals("senior")) return "고령견";
		
		return "";
	}
	
	/* 결제 관련 */
	
	/*
	 * 신용카드: CreditCard
	 * 계좌이체: AccounTranster
	 * 
	 * TODO 다른 할부 개월 목록 더 추가해야
	 */
	public static String translatePaymentType(String type) {
		if(type.equals("CreditCard")) return "신용카드";
		if(type.equals("AccounTranster")) return "계좌이체";
		return "";
	}
	
	/*
	 * 일시불: instance
	 * 2개월: 2M
	 * 3개월: 3M
	 * 
	 * TODO 다른 할부 개월 목록 더 추가해야
	 */
	public static String translateMonthlyInstallment(String type) {
		if(type.equals("instance")) return "일시불";
		if(type.endsWith("M")) return type.replace("M", "개월");
		return "";
	}
	
	/*
	 * 0: 온전한 결제 완료
	 * 1: 결제 취소 상태
	 * 
	 * TODO 다른 할부 개월 목록 더 추가해야
	 */
	public static String translateHasCanceled(int type) {
		if(type == 0) return "결제 완료";
		if(type == 1) return "결제 취소";
		return "";
	}
	
	/*
	 * 배송 상태
	 * PaymentBilled: 결제 완료
	 * PackingFinished: 배송준비 완료
	 * TerminalHub: 터미널 이동
	 * EndPoint: 배송 상차
	 * DeliveryCurrent: 배송중
	 */
	public static String translateStatusDelivery(String status) {
		if(status.equals("PaymentBilled")) return "결제 완료";
		if(status.equals("PackingFinished")) return "배송준비 완료";
		if(status.equals("TerminalHub")) return "터미널 이동";
		if(status.equals("EndPoint")) return "배송 상차";
		if(status.equals("DeliveryCurrent")) return "배송중";
		return "";
	}
}
