package com.givemetreat.interceptor;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
			HttpServletRequest request
			, HttpServletResponse response
			, Object handler) throws IOException {
		String uri = request.getRequestURI();
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		
		if(userId == null && ( uri.startsWith("/pet")
				|| uri.startsWith("/shopping-cart")
				|| uri.startsWith("/invoice")
				|| uri.startsWith("/community")
				)){
			response.sendRedirect("/user/sign-in-view");
			return false;
		}
		
		if(userId != null && uri.startsWith("/user")) {
			response.sendRedirect("/product/product-list-view");
			return false;
		}
		
		//관리자 페이지 권한 설정
		//session: "authorizationCurrent" = "admin"
		String authorizationCurrent = (String) session.getAttribute("authorizationCurrent");
		if(authorizationCurrent == null && uri.startsWith("/admin")) {
			response.sendRedirect("/admin/sign-in-view");
			return false;
		}
		
		return true;
	}
	
	@Override
	public void postHandle(
			HttpServletRequest request
			, HttpServletResponse response
			, Object handler
			, ModelAndView mav) {
		log.info("[Interceptor postHandle]");
	}
	
	@Override
	public void afterCompletion(
			HttpServletRequest request
			, HttpServletResponse response
			, Object handler
			, Exception ex) {
		log.info("[Interceptor afterCompletion]");
	}
}
