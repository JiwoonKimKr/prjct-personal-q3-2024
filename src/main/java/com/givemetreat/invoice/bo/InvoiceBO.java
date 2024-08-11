package com.givemetreat.invoice.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.givemetreat.productShoppingCart.bo.ProductShoppingCartBO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InvoiceBO {
	private final ProductShoppingCartBO productShoppingCartBO;
	
	public List<ProductShoppingCartVO> getListProductsFromCartByUserId(Integer userId) {
		return productShoppingCartBO.getProductsByUserId(userId);
	}

}
