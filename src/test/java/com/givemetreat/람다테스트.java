package com.givemetreat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class 람다테스트 {

	@Test
	void 람다테스트1() {
		//람다, stream
		List<String> fruits = List.of("apple", "banana", "cherry"); //리스트 오브는 자바 12버전부터
		fruits.stream()
			.filter(element -> element.startsWith("b"))//filter라는 stream<T>의 함수!
			.forEach(element -> log.info("[람다테스트: 람다테스트1] 반복문 속 element:{}", element)); //스트림에 있는 요소들을 반복문으로 돌릴 수 있다
	}
	
	@Test
	void 람다테스트2() {
		List<String> fruits = List.of("apple", "banana", "cherry");
		fruits = fruits.stream()
			.map(element -> element.toUpperCase())
			.collect(Collectors.toList()); //다시 리스트로 만들어줘야 쓰기 편하다!
		//여기서 꼭 저장해야;
		
		log.info("[람다테스트: 람다테스트2] fruits:{}", fruits);
	}

}
