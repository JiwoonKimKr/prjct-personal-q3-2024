package com.givemetreat.productBuffer.bo;

import org.springframework.stereotype.Service;

import com.givemetreat.productBuffer.repository.ProductBufferRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class productBufferBO {
	private final ProductBufferRepository productBufferRepository;

	public Integer getCountByProductId(Integer productId) {
		return productBufferRepository.countByProductId(productId);
	}

}
