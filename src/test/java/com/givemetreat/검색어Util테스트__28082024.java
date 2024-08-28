package com.givemetreat;

import org.junit.jupiter.api.Test;

import com.givemetreat.common.utils.SearchMapFromKeyword;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class 검색어Util테스트__28082024 {

	@Test
	void 검색어테스트() {
		String keyword="서리태 사료 treat 간식 어린 약한 튼튼 강아지 망고";
		
		SearchMapFromKeyword searchMap = new SearchMapFromKeyword(keyword);
		
		log.info("[검색어테스트] keyword:{}", keyword);
		log.info("[검색어테스트] searchMap:{}", searchMap);
	}

}
