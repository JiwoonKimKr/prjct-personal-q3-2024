package com.givemetreat.common.validation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvoiceParamsValidation {

	public static Boolean getParamsValidated(Integer payment
											, String buyerName
											, String buyerPhoneNumber
											, String receiverName
											, String receiverPhoneNumber
											, String address) {
		
		//payment
		if(WordingValidation.isNumeric(payment.toString()) == false) {
			log.warn("[InvoiceParamsValidation] payment is not numeric. payment:{}", payment);
			return false;
		}
		/*  포트원 결제하면서 수정_21 08 2024
		//paymentType
		if(paymentType.equals("CreditCard") == false
			&& paymentType.equals("AccountTranster") == false) {
			log.warn("[InvoiceParamsValidation] paymentType isn't correct. paymentType:{}", paymentType);
			return false;
		}
		//company
		List<String> listCompanies = StringTranslator.listCreditCardCompaniesE();
		boolean isCompanyCorrent = false;
		for(String title : listCompanies) {
			if(company.equals(title) == true) {
				isCompanyCorrent = true;
				break;
			}
		}
		if(isCompanyCorrent == false) {
			log.warn("[InvoiceParamsValidation] company name isn't correct. company:{}", company);
			return false;
		}
		
		//monthlyInstallment
		if(monthlyInstallment.equals("instance") == false) {
			if(monthlyInstallment.endsWith("M") == false) {
				log.warn("[InvoiceParamsValidation] char 'M', end of monthlyInstallment isn't correct. monthlyInstallment:{}", monthlyInstallment);
				return false;
			}
			String substractedM = monthlyInstallment.substring(0, monthlyInstallment.length()-1);
			if(WordingValidation.isNumeric(substractedM) == false) {
				log.warn("[InvoiceParamsValidation] numeric value of monthlyInstallment isn't correct. monthlyInstallment:{}", monthlyInstallment);
				return false;
			}
		}
		*/
		//buyerName
		if(WordingValidation.isAlpha(buyerName) == false
				&& WordingValidation.isKorean(buyerName) == false) {
			log.warn("[InvoiceParamsValidation] buyerName isn't correct. buyerName:{}", buyerName);
			return false;
		}
		//buyerPhoneNumber
		if(WordingValidation.isMobilePhone(buyerPhoneNumber) == false) {
			log.warn("[InvoiceParamsValidation] buyerPhoneNumber isn't correct. buyerPhoneNumber:{}", buyerPhoneNumber);
			return false;
		}
		//receiverName
		if(WordingValidation.isAlpha(receiverName) == false
				&& WordingValidation.isKorean(receiverName) == false) {
			log.warn("[InvoiceParamsValidation] receiverName isn't correct. receiverName:{}", receiverName);
			return false;
		}
		//receiverPhoneNumber
		if(WordingValidation.isMobilePhone(receiverPhoneNumber) == false) {
			log.warn("[InvoiceParamsValidation] receiverPhoneNumber isn't correct. receiverPhoneNumber:{}", receiverPhoneNumber);
			return false;
		}
		
		//address 주소는 추후 addressLine1와 addressLine2로 나뉨, 그래서 빈칸으로 나눈 배열 길이가 최소 3-4는 넘어야
		//TODO address로 넘어온 String 값들을 직접 점검 필요
		if(address.split(" ").length < 3) {
			log.warn("[InvoiceParamsValidation] receiverPhoneNumber isn't correct. receiverPhoneNumber:{}", receiverPhoneNumber);
			return false;
		}
		return true;
	}
}
