package com.givemetreat.productBuffer.bo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class ProductBufferBOTest {
	@Autowired
	ProductBufferBO ProductBufferBO;
	
//	@Test
//	void jUnitTest_getAvailableStockByProductId() {
//		int stockAvailable = ProductBufferBO.getCountAvailableByProductIdAndReserved(6, false);
//		
//		log.info("[productBufferBOTest jUnitTest_getAvailableStockByProductId()]");
//		assertEquals(stockAvailable, 0);
//	}
	@Test
	void jUnitTest_getBufferTest() {
		Integer item = ProductBufferBO.getCount(5, true, 3);
		
		log.info("[productBufferBOTest jUnitTest_getBufferTest()]");
		assertEquals(item, 5);
	}

}
