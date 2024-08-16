package com.givemetreat.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect // 부가 기능 정의(advice) + 어디에 적용(pointcut)
@Component
public class PagingAop {

	//@Around("execution(* com.givemetreat..*(..)") //패키지의 범위 지정 => 어디에 적용할지(pointcut)
	@Around("@annotation(Paging)") //어느 어노테이션이 붙어있을 때만 수행한다는 뜻!
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		Object target = joinPoint.getTarget();
		List<Object> listArgs = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
		
		log.info("[⚠️⚠️⚠️⚠️⚠️PagingAop execute] target:{}, listArgs:{}",target, listArgs);
		
		Object proceedObj = joinPoint.proceed(); // 본래 할 일을 수행; 수행한 결과를 Object로 저장;
		
		return proceedObj;
	}
}
