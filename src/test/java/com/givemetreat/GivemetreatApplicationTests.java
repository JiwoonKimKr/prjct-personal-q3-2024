package com.givemetreat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest //이건 스프링 서버를 모두 불러오는 어노테이션
class GivemetreatApplicationTests {

	@Test
	void 더하기테스트() { //한글로 명칭을 작성 가능! 테스트 전용 메소드라는 걸 바로 알 수 있다!
		int x = 15;
		int y = 20;
		assertEquals(x, y, 35); //뒤에 정답을 적어야 확인 가능하다! 
	}

}
