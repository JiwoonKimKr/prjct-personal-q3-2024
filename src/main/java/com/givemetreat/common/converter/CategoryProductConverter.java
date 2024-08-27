package com.givemetreat.common.converter;

import com.givemetreat.product.domain.CategoryProduct;

import jakarta.persistence.AttributeConverter;

public class CategoryProductConverter implements AttributeConverter<CategoryProduct, String> {

	@Override
	public String convertToDatabaseColumn(CategoryProduct category) {
		return category.getTypeE();
	}

	@Override
	public CategoryProduct convertToEntityAttribute(String dbData) {
		return CategoryProduct.findCategoryProduct(dbData, null, null);
	}
	

}
