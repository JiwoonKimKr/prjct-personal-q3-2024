package com.givemetreat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class Test_BinarySearchTree {

	@Test
	void test() {
		//given
		int number = 544;
		
		List<Integer> list = new ArrayList<>();
		
		for(int i = 0; i < 1000; i ++) {
			list.add(i);
		}
		
		log.info("[Test_BinarySearchTree test()] list for test:{}", list.size());
		//when
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
		
		int length = list.size() - 1;
		Integer indexMax = length;
		Integer indexMin = 0; 
		Integer indexCurrent;
		if(list.get(length) == number) {
			return;
		}
		
		while(true) {
			indexCurrent = (indexMax + indexMin) / 2;
			Integer valueCurrent = list.get(indexCurrent);
			if(valueCurrent == number) {
				break;
			}
			if(number > valueCurrent) {
				indexMin = indexCurrent;
			} else {
				indexMax = indexCurrent;
			}
			
			log.info("[Test_BinarySearchTree test()] indexCurrent:{}", indexCurrent);
		}
		//then
//		stopWatch.stop();
//		log.info("[Test_BinarySearchTree test()] execution time:{} ms", stopWatch.getTotalTimeMillis());
//		log.info(stopWatch.prettyPrint());
	
	}

}
