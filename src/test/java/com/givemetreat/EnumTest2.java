package com.givemetreat;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

class EnumTest2 {
	public enum CalcType{
		//열거형 정의
		//람다식Lambda Expression으로 함수 활용 가능!
		CALC_A(value -> value)
		, CALC_B(value -> value*10)
		, CALC_C(value -> value*3)
		, CALC_ETC(value -> 0);
		
		// 필드 정의 => enum에 정의된 function
		//Function <T_input, T_output> methodName 방식으로
		private Function<Integer, Integer> expression;
		
		//생성자
		CalcType(Function<Integer, Integer> expression){
			this.expression = expression;
		}
		
		// 계산 적용 메소드; 이제 enum의 표현식을 실행시키는 메소드를 기재해줘야!
		public int calculate(int value) {
			return expression.apply(value);
		}
	}
	
	@Test
	void 계산테스트() {
		//given
		CalcType ctype = CalcType.CALC_B;
		//when
		int result = ctype.calculate(500);
		//then
		assertEquals(5000, result);
	}
}
