package com.givemetreat.productShoppingCart.bo;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.givemetreat.product.bo.ProductBO;
import com.givemetreat.product.domain.ProductVO;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartEntity;
import com.givemetreat.productShoppingCart.domain.ProductShoppingCartVO;
import com.givemetreat.productShoppingCart.repository.ProductShoppingCartRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
			ProductVO product = productBO.getProducts(
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
	@Transactional
	public ProductShoppingCartEntity updateQuantity(int userId, int productId, int quantity, Integer id){
		
		//막 넣으면 안 된다 ㅠㅠ 13:10_10 08 2024
		//productBuffer와 달리 사용자 장바구니는 Column(Field)에 갯수를 기입한다.
		//따라서 userId와 productId가 중복되는 경우는 존재할 수 없다!
		
		ProductShoppingCartEntity entity = productShoppingCartRepository.findByUserIdAndProductId(userId, productId);
		
		if(ObjectUtils.isEmpty(id) == false) {
			ProductShoppingCartEntity entityForValidation = productShoppingCartRepository.findById(id).orElse(null);
			if(entity.equals(entityForValidation) == false) {
				log.warn("[ProductShoppingCartBO addProductsByProductIdAndQuantity()]"
						+ " A Record with current userId & productId doesn't match with primary key."
						+ " userId:{}, productId:{}, quantity:{}, id:{}", userId, productId, quantity, id);
				return null;
			}
		}
		
		if(ObjectUtils.isEmpty(entity) == false) {
			log.info("[ProductShoppingCartBO addProductsByProductIdAndQuantity()]"
					+ " A Record for current arguments was already exist."
					+ " userId:{}, productId:{}, quantity:{}", userId, productId, quantity);
			entity = entity.toBuilder()
						.quantity(quantity)
						.build();
			//quantity가 0일 경우 db에서 삭제해야
			if(quantity == 0) {
				deleteEntity(entity);
				return entity;
			}
			
			productShoppingCartRepository.save(entity);
			return entity;
		}
		
		log.info("[ProductShoppingCartBO addProductsByProductIdAndQuantity()]"
				+ "No record has found, so new one get made."
				+ " userId:{}, productId:{}, quantity:{}", userId, productId, quantity);		
		return productShoppingCartRepository.save(ProductShoppingCartEntity.builder()
																		.userId(userId)
																		.productId(productId)
																		.quantity(quantity)
																		.build());
	}

	@Transactional
	private void deleteEntity(ProductShoppingCartEntity entity) {
		log.info("[ProductShoppingCartBO addProductsByProductIdAndQuantity()]"
				+ " current entity get deleted."
				+ " entity:{}", entity);
		productShoppingCartRepository.delete(entity);
	}

	@Transactional
	public boolean emptyCart(Integer userId) {
		List<ProductShoppingCartEntity> listEntities = productShoppingCartRepository.findByUserId(userId);
		
		if(ObjectUtils.isEmpty(listEntities) == true) {
			log.warn("[ProductShoppingCartBO emptyCart()]"
					+ " current cart of userId got null result."
					+ " userId:{}", userId);
			return false;
		}
		
		productShoppingCartRepository.deleteAllInBatch(listEntities);
		
		log.info("[ProductShoppingCartBO emptyCart()]"
				+ " current cart of userId get erased."
				+ " userId:{}", userId);
		return true;
	}
	
}
