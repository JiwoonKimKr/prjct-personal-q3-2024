package com.givemetreat.pet.domain;

import org.springframework.util.ObjectUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum AgePet {
	under6months("under6months", "6개월 미만", 0)
	, adult("adult", "성견", 1)
	, senior("senior", "고령견", 2);

	@Getter
	private final String agePetE;
	@Getter
	private final String agePetK;
	@Getter
	private final int agePetCode;
	
	AgePet(String agePetE, String agePetK, int agePetCode){
		this.agePetE = agePetE;
		this.agePetK = agePetK;
		this.agePetCode = agePetCode;
	}
	
	public static AgePet findAgeCurrent(String agePetE, String agePetK, Integer agePetCode) {
		AgePet[] arrAgePet = AgePet.values();
		for(AgePet age : arrAgePet) {
			if(ObjectUtils.isEmpty(agePetE) == false && agePetE.equals(age.getAgePetE())) {
				return age;
			}
			if(ObjectUtils.isEmpty(agePetK) == false && agePetK.equals(age.getAgePetK())) {
				return age;
			}
			if(ObjectUtils.isEmpty(agePetCode) == false && agePetCode == age.getAgePetCode()) {
				return age;
			}
		}
		log.warn("[AgePet findAgeCurrent()] Enum value of AgePet not found. agePetE:{}, agePetK:{}, agePetCode:{}", agePetE, agePetK, agePetCode);
		return null;
	}
}
