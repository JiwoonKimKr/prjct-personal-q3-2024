package com.givemetreat.common.validation;

public class AdminProductParamsValidation {

	public static String getParamsValidated(String name
											, String category
											, String price
											, String agePetProper
											, String quantity) {
		//상품명 길이가 24자 초과되면 반려시켜야
		if(name.length() > 24) {
			return name;
		}
		//제품 카테고리가 kibble이나 treat이 아니면
		if(category.equals("kibble") == false
				&& category.equals("treat") == false) {
			return category;
		}
		//price 가격이 숫자가 아니면
		if(WordingValidation.isNumeric(price) == false) {
			return price;
		}
		//agePetProper 변수가 해당 값이 아니면
		if(agePetProper.equals("under6months") == false
				&& agePetProper.equals("adult") == false
				&& agePetProper.equals("senior") == false) {
			return agePetProper;
		}
		//quantity 수량이 숫자가 아니면
		if(WordingValidation.isNumeric(quantity) == false) {
			return quantity;
		}
		
		return null;
	}

}
