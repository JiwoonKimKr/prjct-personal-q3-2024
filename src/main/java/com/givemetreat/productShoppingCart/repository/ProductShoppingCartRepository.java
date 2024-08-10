package com.givemetreat.productShoppingCart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.givemetreat.productShoppingCart.domain.ProductShoppingCartEntity;

public interface ProductShoppingCartRepository extends JpaRepository<ProductShoppingCartEntity, Integer> {

	List<ProductShoppingCartEntity> findByUserId(int userId);

	ProductShoppingCartEntity findByUserIdAndProductId(int userId, int productId);

}
