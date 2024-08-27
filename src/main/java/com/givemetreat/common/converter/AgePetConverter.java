package com.givemetreat.common.converter;

import com.givemetreat.pet.domain.AgePet;

import jakarta.persistence.AttributeConverter;

public class AgePetConverter implements AttributeConverter<AgePet, String> {
	@Override
	public String convertToDatabaseColumn(AgePet agePet) {
		return agePet.getAgePetE();
	}

	@Override
	public AgePet convertToEntityAttribute(String dbData) {
		return AgePet.findAgeCurrent(dbData, null, null);
	}
}
