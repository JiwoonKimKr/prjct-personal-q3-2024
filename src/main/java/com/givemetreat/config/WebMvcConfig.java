package com.givemetreat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.givemetreat.common.FileManagerService;
import com.givemetreat.interceptor.PermissionInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
	private final PermissionInterceptor permissionInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
		.addInterceptor(permissionInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/error", "/css/**", "/img/**", "/user/sign-out");
	}
	
	@Override
	public void addResourceHandlers(
			ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/images/**")
		.addResourceLocations("file:///" + FileManagerService.FILE_UPLOAD_PATH);
	}
}
