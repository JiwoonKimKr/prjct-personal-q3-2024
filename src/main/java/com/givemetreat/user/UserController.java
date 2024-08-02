package com.givemetreat.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	
	@GetMapping("/")
	public String signInView() {
		return "user/signIn";
	}

	@GetMapping("/user/sign-up-view")
	public String signUpView() {
		return "user/signUp";
	}
	
	@GetMapping("/user/kakao-api")
	public String kakaoApi(){
		return "redirect:https://kauth.kakao.com/oauth/authorize?" 
				+ "response_type=code"
				+ "&client_id=aade720b5d42c7f112e4f9be91c63a27"
				+ "&redirect_uri=http://localhost/OAuth/kakao-sign-up";
	}
}
