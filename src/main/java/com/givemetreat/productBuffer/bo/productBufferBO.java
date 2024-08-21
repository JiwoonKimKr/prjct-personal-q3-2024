package com.givemetreat.productBuffer.bo;

import java.util.*;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.givemetreat.productBuffer.domain.ProductBufferEntity;
import com.givemetreat.productBuffer.repository.ProductBufferRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductBufferBO {
	private final ProductBufferRepository productBufferRepository;

	//해당 상품 총 재고 수량_배송 예약된 수량 포함_08 08 2024
	public Integer getTotalCountByProductId(Integer productId) {
		return productBufferRepository.countByProductId(productId);
	}
	
	//해당 상품 가용 가능 수량_배송 예약된 수량 제외_08 08 2024
	public Integer getCountAvailableByProductIdAndReserved(Integer productId, boolean reserved) {
		return productBufferRepository.countByProductIdAndReserved(productId, reserved);
	}	

	@Transactional
	public List<ProductBufferEntity> addProductBuffersInQuantity(
												Integer productId
												, Integer quantity){
		log.info("[productBufferBO: addProductBuffersInQuantity() Requested] productId:{}, quantity:{}"
					, productId, quantity);
		List<ProductBufferEntity> listEntities = new ArrayList<>();
		ProductBufferEntity entity = ProductBufferEntity.builder()
				.productId(productId)
				.reserved(false)
				.build();
		
		//해당 수량만큼 entity를 생성해서 DB에 저장
		for(int i = 0; i < quantity; i ++) {
			productBufferRepository.save(entity);
			
			listEntities.add(entity);
		}
		
		log.info("[productBufferBO: addProductBuffersInQuantity() completed] productId:{}, quantity:{}"
				, productId, quantity);

		return listEntities;
	}

	@Transactional
	public Integer getCount(int productId, boolean reserved, int productInvoiceId) {
		return productBufferRepository.countByProductIdAndReservedAndProductInvoiceId(productId, reserved, productInvoiceId);
	}

	@Transactional
	public List<ProductBufferEntity> updateProductBuffersInQuantity(int productId, Integer quantity,
			int productInvoiceId) {
		
		List<ProductBufferEntity> listBuffers = productBufferRepository.findByProductIdAndReservedOrderById(productId, false, Limit.of(quantity));
		if(ObjectUtils.isEmpty(listBuffers) || listBuffers.size() != quantity) {
			log.warn("[productBufferBO: updateProductBuffersInQuantity()]"
					+ " failed to select product_buffer list. productId:{}", productId);
			return null;
		}
		for(ProductBufferEntity entity : listBuffers) {
			productBufferRepository.save(entity.toBuilder()
					.reserved(true)
					.productInvoiceId(productInvoiceId)
					.build());
		}
		log.info("[productBufferBO: updateProductBuffersInQuantity()]"
				+ " List<ProductBufferEntity> get reserved. List<ProductBufferEntity>:{}", listBuffers);
		
		return listBuffers;
	}

	public List<ProductBufferEntity> resetProductBuffersByProductInvoiceId(int productInvoiceId) {
		List<ProductBufferEntity> listBuffers = productBufferRepository.findByProductInvoiceId(productInvoiceId);
		if(ObjectUtils.isEmpty(listBuffers)) {
			log.warn("[productBufferBO: resetProductBuffersByProductInvoiceId()]"
					+ " failed to select product_buffer list. productInvoiceId:{}", productInvoiceId);
			return null;
		}
		for(ProductBufferEntity entity : listBuffers) {
			productBufferRepository.save(entity.toBuilder()
									.reserved(false)
									.productInvoiceId(null)
									.build());
		}
		log.info("[productBufferBO: resetProductBuffersByProductInvoiceId()]"
				+ " List<ProductBufferEntity> get reseted as status not reserved. List<ProductBufferEntity>:{}", listBuffers);
		
		return listBuffers;
	}

}
