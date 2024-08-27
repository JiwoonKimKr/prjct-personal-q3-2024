package com.givemetreat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MethodReference {

	@ToString
	@AllArgsConstructor
	public class Person{
		private String name;
		private int age;
		
		public void printInfo() {
			log.info("[🚧🚧🚧🚧🚧MethodReference] {}", this);
		}
	}
	
	
	@Test
	void 메소드레퍼런스테스트() {
		List<Person> people = new ArrayList<>();
		people.add(new Person("서리태", 4));
		people.add(new Person("왕다롱", 17));
		
		/* 객체 안에 있는 메소드 호출 */
		//people.forEach(p -> p.printInfo()); //람다
		//people.forEach(Person::printInfo); //메소드 레퍼런스
		

		//간단하게 활용할 때는 이렇게 쓸 수 있다!
		

		/*객체를 println으로 출력하는 방법*/
		//people.forEach(p -> System.out.println(p));
		people.forEach(System.out::println); //메소드 레퍼런스로 표현할 수 있다.
		//system.out이 하나의 객체라고 볼 수 있다고 하심.
	}
}
