package com.givemetreat.productShoppingCart.bo;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.givemetreat.product.bo.ProductBO;
import com.givemetreat.product.domain.ProductVO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartEntity;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartVO;
import com.givemetreat.productShoppingCart.repository.ProductShoppingCartRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductShoppingCartBO {
	private final ProductShoppingCartRepository productShoppingCartRepository;
	private final ProductBO productBO;

	/**
	 * userId에 해당하는 사용자의 장바구니 조회
	 * @param userId
	 * @return
	 */
	@Transactional
	public List<ProductShoppingCartVO> getProductsByUserId(int userId) {
		List<ProductShoppingCartEntity> listItems =  productShoppingCartRepository.findByUserId(userId);
		List<ProductShoppingCartVO> listRecords = new ArrayList<>();
		
		for(ProductShoppingCartEntity record : listItems) {
			ProductVO product = productBO.getProduct(
								record.getProductId(), null, null, null, null)
								.get(0);
			listRecords.add(new ProductShoppingCartVO(record, product));
		}
		
		return listRecords;
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
