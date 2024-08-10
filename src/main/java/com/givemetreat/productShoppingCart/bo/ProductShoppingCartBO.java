package com.givemetreat.productShoppingCart.bo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.givemetreat.productShoppingCart.domain.ProductShoppingCartEntity;
import com.givemetreat.productShoppingCart.repository.ProductShoppingCartRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductShoppingCartBO {
	private final ProductShoppingCartRepository productShoppingCartRepository;

	/**
	 * userId에 해당하는 사용자의 장바구니 조회
	 * @param userId
	 * @return
	 */
	public List<ProductShoppingCartEntity> getProductsByUserId(int userId) {
		return productShoppingCartRepository.findByUserId(userId);
	}
	
	/**
	 * userId에 해당하는 사용자의 장바구니에 
	 * 해당 상품의 PK (Product Id)와 수량(quantity)을 DB에 기입한 후, List<>로 반환한다.
	 * @param productId
	 * @param quantity
	 * @return
	 */
	public ProductShoppingCartEntity addProductsByProductIdAndQuantity(int userId, int productId, int quantity){
		
		return productShoppingCartRepository.save(ProductShoppingCartEntity.builder()
																		.userId(userId)
																		.productId(productId)
																		.quantity(quantity)
																		.build());
	}
	
}
