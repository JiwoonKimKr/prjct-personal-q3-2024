package com.givemetreat.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.givemetreat.common.utils.StopWatchUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class TimeTraceCustomAop {
	private final StopWatchUtil sw;

	@Around("@annotation(TimeTraceStart)")
	public Object traceTimeStarted(ProceedingJoinPoint joinPoint) throws Throwable {
		if(sw.isRunning()) {
			log.info("[⚠️⚠️⚠️⚠️⚠️TimeTraceAop execute] StopWatchUtil has already activated. StopWatch ID: {}", sw.getId());
			sw.stop();
		}
		
		//API가 호출되는 시점에서 시간 측정 시작;
		sw.start();
		sw.setKeepTaskList(true);
		
		Object proceedObj = joinPoint.proceed(); // 본래 할 일을 수행; 수행한 결과를 Object로 저장;
		
		log.info("[⚠️⚠️⚠️⚠️⚠️TimeTraceAop execute]  StopWatchUtil has activated. StopWatch ID:{}", sw.getId());
		return proceedObj;
	}
	
	@Around("@annotation(TimeTraceStop)")
	public Object traceTimeStoped(ProceedingJoinPoint joinPoint) throws Throwable {
		
		//API가 호출되는 시점에서 시간 측정 시작;
		sw.stop();
		log.info("[⚠️⚠️⚠️⚠️⚠️TimeTraceAop execute] userId deactivate stopwatch. Time spended in ms:{} ms", sw.getTotalTimeMillis());
		sw.prettyPrint();
		
		Object proceedObj = joinPoint.proceed(); // 본래 할 일을 수행; 수행한 결과를 Object로 저장;
		
		if(sw.isRunning() == true) {
			log.warn("[⚠️⚠️⚠️⚠️⚠️TimeTraceAop execute] Stopwatch didn't get stopped. id:{}", sw.getId());
		}
		
		return proceedObj;
	}	
}
