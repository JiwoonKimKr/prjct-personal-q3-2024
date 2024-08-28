package com.givemetreat.common.converter;

import org.springframework.util.ObjectUtils;

import com.givemetreat.product.domain.CategoryProduct;

import jakarta.persistence.AttributeConverter;

public class CategoryProductConverter implements AttributeConverter<CategoryProduct, String> {

	@Override
	public String convertToDatabaseColumn(CategoryProduct category) {
		if(ObjectUtils.isEmpty(category)) {
			return null;
		}
		return category.getTypeE();
	}

	@Override
	public CategoryProduct convertToEntityAttribute(String dbData) {
		return CategoryProduct.findCategoryProduct(dbData, null, null);
	}
	

}
