package com.givemetreat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.givemetreat.pet.domain.AgePet;

@SpringBootTest
class Test_27_08_2024 {

	@Test
	void test() {
		AgePet age = AgePet.adult;
		String agePetE = age.getAgePetE();
		String agePetK = age.getAgePetK();
		
		assertEquals("adult", agePetE);
		assertEquals("성견", agePetK);
	}

}
