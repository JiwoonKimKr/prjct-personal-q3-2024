package com.givemetreat.common.validation;

import java.util.*;

import com.givemetreat.common.utils.StringTranslator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvoiceParamsValidation {

	public static Boolean getParamsValidated(Integer payment
											, String paymentType
											, String company
											, String monthlyInstallment
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
				log.warn("[InvoiceParamsValidation] monthlyInstallment isn't correct. monthlyInstallment:{}", monthlyInstallment);
				return false;
			}
			String substractedM = monthlyInstallment.substring(monthlyInstallment.length()-1);
			if(WordingValidation.isNumeric(substractedM) == false) {
				log.warn("[InvoiceParamsValidation] monthlyInstallment isn't correct. monthlyInstallment:{}", monthlyInstallment);
				return false;
			}
		}
		
		//buyerName
		if(WordingValidation.isAlphaNumeric(buyerName) == false) {
			log.warn("[InvoiceParamsValidation] buyerName isn't correct. buyerName:{}", buyerName);
			return false;
		}
		//buyerPhoneNumber
		if(WordingValidation.isMobilePhone(buyerPhoneNumber) == false) {
			log.warn("[InvoiceParamsValidation] buyerPhoneNumber isn't correct. buyerPhoneNumber:{}", buyerPhoneNumber);
			return false;
		}
		//receiverName
		if(WordingValidation.isAlphaNumeric(receiverName) == false) {
			log.warn("[InvoiceParamsValidation] receiverName isn't correct. receiverName:{}", receiverName);
			return false;
		}
		//receiverPhoneNumber
		if(WordingValidation.isMobilePhone(receiverPhoneNumber) == false) {
			log.warn("[InvoiceParamsValidation] receiverPhoneNumber isn't correct. receiverPhoneNumber:{}", receiverPhoneNumber);
			return false;
		}
		
		//address
			//★★★★★ 주소 검증하는 장치 마련해야 ㅠㅡㅜ
		return true;
	}
}
