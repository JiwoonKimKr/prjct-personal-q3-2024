package com.givemetreat.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.givemetreat.userFavorite.bo.UserFavoriteBO;
import com.givemetreat.userFavorite.domain.UserFavoriteEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class TrackUsersFavorAop {
	private final UserFavoriteBO userFavoriteBO;
	
	@Around("@annotation(TrackUsersFavor)")
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Object[] targetParameters = joinPoint.getArgs();

		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
		
		Integer userId = (Integer) session.getAttribute("userId") ;
		//session 없는 경우 리턴
		if(ObjectUtils.isEmpty(userId)) {
			return joinPoint.proceed(); 
		}
		
		int countNull = 0;
		for(Object parameter : targetParameters) {
			if(ObjectUtils.isEmpty(parameter)
					|| parameter instanceof String == false) {
				countNull ++;
				continue;
			}
			String paramCurrent = parameter.toString();
			
			if(paramCurrent.equals("kibble")
					|| paramCurrent.equals("treat")) {
				log.info("[💡💡💡💡💡TrackUsersFavorAop execute()] current Category parameter:{}", paramCurrent);
				
				UserFavoriteEntity entity = userFavoriteBO.updateUserFavors(userId, paramCurrent, null); 
				
				if(ObjectUtils.isEmpty(entity)) {
					log.warn("[⚠️⚠️⚠️⚠️⚠️TrackUsersFavorAop execute()] entity for userFavorite table not made. parameter:{}", paramCurrent);
				}
				
			}
			if(paramCurrent.equals("under6months")
					|| paramCurrent.equals("adult")
					|| paramCurrent.equals("senior")) {
				log.info("[💡💡💡💡💡TrackUsersFavorAop execute()] current AgePetProper parameter:{}", paramCurrent);
				
				UserFavoriteEntity entity = userFavoriteBO.updateUserFavors(userId, null, paramCurrent);
				
				if(ObjectUtils.isEmpty(entity)) {
					log.warn("[⚠️⚠️⚠️⚠️⚠️TrackUsersFavorAop execute()] entity for userFavorite table not made. parameter:{}", paramCurrent);
				}
			}
		}
		//들어온 변수 없는 경우 리턴
		if(targetParameters.length == countNull) {
			return joinPoint.proceed();
		}
		
		return joinPoint.proceed();
	}
}
