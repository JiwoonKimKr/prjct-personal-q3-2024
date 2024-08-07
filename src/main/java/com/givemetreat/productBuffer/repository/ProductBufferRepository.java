package com.givemetreat.productBuffer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.productBuffer.domain.ProductBufferEntity;

public interface ProductBufferRepository extends JpaRepository<ProductBufferEntity, Integer> {

	Integer countByProductId(Integer productId);

}
