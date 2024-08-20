package com.givemetreat.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class UserController {

	@GetMapping("/sign-in-view")
	public String signInView() {
		return "user/signIn";
	}	
	
	@GetMapping("/sign-up-view")
	public String signUpView() {
		return "user/signUp";
	}
	
	@GetMapping("/kakao-api")
	public String kakaoApi(){
		return "redirect:https://kauth.kakao.com/oauth/authorize?" 
				+ "response_type=code"
				+ "&client_id=aade720b5d42c7f112e4f9be91c63a27"
				+ "&redirect_uri=http://localhost/OAuth/kakao-sign-up";
	}
	
	@RequestMapping("/sign-out")
	public String signOut(HttpSession session) {
		session.removeAttribute("loginId");
		session.removeAttribute("userId");
		session.removeAttribute("userName");
		
		return "redirect:/user/sign-in-view";
	}
	
	@GetMapping("/verify-code-view")
	public String verifyCodeView() {
		return "user/verifyCode";
	}
}
