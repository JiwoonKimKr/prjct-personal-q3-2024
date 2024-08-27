package com.givemetreat.product.domain;

import org.springframework.util.ObjectUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum CategoryProduct {
	kibble("kibble", "사료", 0)
	, treat("treat", "간식", 1);

	private String typeE;
	private String typeK;
	private Integer index;
	
	CategoryProduct(String typeE, String typeK, Integer index){
		this.typeE = typeE;
		this.typeK = typeK;
		this.index = index;
	}
	
	public static CategoryProduct findCategoryProduct(String typeE, String typeK, Integer index) {
		CategoryProduct[] arrCategory = CategoryProduct.values();
		for(CategoryProduct type : arrCategory) {
			if(ObjectUtils.isEmpty(typeE) == false && typeE.equals(type.getTypeE())) {
				return type;
			}
			if(ObjectUtils.isEmpty(typeK) == false && typeK.equals(type.getTypeK())) {
				return type;
			}
			if(ObjectUtils.isEmpty(index) == false && index == type.getIndex()) {
				return type;
			}
		}
		log.warn("[CategoryProduct findCategoryProduct()] Enum value of CategoryProduct not found."
				+ " typeE:{}, typeK:{}, index:{}", typeE, typeK, index);
		return null;
	}
}
