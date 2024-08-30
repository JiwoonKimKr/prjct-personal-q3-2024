package com.givemetreat.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.givemetreat.pet.domain.AgePet;
import com.givemetreat.product.domain.CategoryProduct;
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
		
		for(Object parameter : targetParameters) {
			if(ObjectUtils.isEmpty(parameter)
					|| parameter instanceof Enum == false) {
				continue;
			}
			
			if(parameter instanceof CategoryProduct) {
				
				log.info("[💡💡💡💡💡TrackUsersFavorAop execute()] current Category parameter:{}", parameter);
				
				UserFavoriteEntity entity = userFavoriteBO.updateUserFavors(userId, parameter, null); 
				
				if(ObjectUtils.isEmpty(entity)) {
					log.warn("[⚠️⚠️⚠️⚠️⚠️TrackUsersFavorAop execute()] entity for userFavorite table not made. parameter:{}", parameter);
				}
				
			}
			if(parameter instanceof AgePet) {
				log.info("[💡💡💡💡💡TrackUsersFavorAop execute()] current AgePetProper parameter:{}", parameter);
				
				UserFavoriteEntity entity = userFavoriteBO.updateUserFavors(userId, null, parameter);
				
				if(ObjectUtils.isEmpty(entity)) {
					log.warn("[⚠️⚠️⚠️⚠️⚠️TrackUsersFavorAop execute()] entity for userFavorite table not made. parameter:{}", parameter);
				}
			}
		}
		
		return joinPoint.proceed();
	}
}
