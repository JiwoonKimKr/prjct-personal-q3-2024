package com.givemetreat.productBuffer.repository;

import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.productBuffer.domain.ProductBufferEntity;

public interface ProductBufferRepository extends JpaRepository<ProductBufferEntity, Integer> {

	Integer countByProductId(Integer productId);

	Integer countByProductIdAndReserved(Integer productId, boolean reserved);

	Integer countByProductIdAndReservedAndProductInvoiceId(int productId, boolean reserved, int productInvoiceId);

	List<ProductBufferEntity> findByProductIdAndReservedOrderById(int productId, boolean reserved, Limit limit);

	List<ProductBufferEntity> findByProductInvoiceId(int productInvoiceId);

	List<ProductBufferEntity> findByProductId(int productId);

}
