package com.givemetreat.common.converter;

import org.springframework.util.ObjectUtils;

import com.givemetreat.invoice.domain.HasCanceled;

import jakarta.persistence.AttributeConverter;

public class HasCanceledConverter implements AttributeConverter<HasCanceled, Integer> {

	@Override
	public Integer convertToDatabaseColumn(HasCanceled hasCanceled) {
		if(ObjectUtils.isEmpty(hasCanceled)) {
			return null;
		}
		return hasCanceled.getIndex();
	}

	@Override
	public HasCanceled convertToEntityAttribute(Integer dbData) {
		return HasCanceled.findIfPaymentCanceled(dbData, null);
	}
}
