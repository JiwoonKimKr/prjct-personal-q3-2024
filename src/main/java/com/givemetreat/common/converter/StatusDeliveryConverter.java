package com.givemetreat.common.converter;

import com.givemetreat.invoice.domain.StatusDelivery;

import jakarta.persistence.AttributeConverter;

public class StatusDeliveryConverter implements AttributeConverter<StatusDelivery, String> {

	@Override
	public String convertToDatabaseColumn(StatusDelivery statusDelivery) {
		return statusDelivery.getStatusE();
	}

	@Override
	public StatusDelivery convertToEntityAttribute(String dbData) {
		return StatusDelivery.findStatusDelivery(dbData, null, null);
	}

}
