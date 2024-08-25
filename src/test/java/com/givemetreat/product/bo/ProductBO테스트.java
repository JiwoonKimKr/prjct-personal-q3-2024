package com.givemetreat.product.bo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.givemetreat.product.domain.ProductVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class ProductBO테스트 {
	@Autowired
	private ProductBO productBO;

	@Test
	void testGetListProductsRecommended() {
		//given
		Integer userId = 4;
		String category = "treat";
		String agePetProper = "adult";
		
		//when
		List<ProductVO> listVOs = productBO.getListProductsRecommended(userId, category, agePetProper);
		
		log.info("[💡💡💡💡💡ProductBO테스트] list<ProductVO> :{}", listVOs);
		 
		//then
		assertEquals(4, listVOs.size());
	}

}
