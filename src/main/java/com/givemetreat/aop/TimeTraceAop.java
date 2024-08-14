package com.givemetreat.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect // 부가 기능 정의(advice) + 어디에 적용(pointcut)
@Component
public class TimeTraceAop {

	//@Around("execution(* com.givemetreat..*(..)") //패키지의 범위 지정 => 어디에 적용할지(pointcut)
	@Around("@annotation(TimeTrace)") //어느 어노테이션이 붙어있을 때만 수행한다는 뜻!
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		//API가 호출되는 시점에서 시간 측정 시작;
		StopWatch sw = new StopWatch(); //스프링에 내장되어 있는 StopWatch라는 클래스
		sw.start();
		
		Object proceedObj = joinPoint.proceed(); // 본래 할 일을 수행; 수행한 결과를 Object로 저장;
		
		sw.stop();
		
		log.info("[⚠️⚠️⚠️⚠️⚠️TimeTraceAop execute] execution time:{} ms", sw.getTotalTimeMillis());
		log.info(sw.prettyPrint());
		return proceedObj;
	}
}
