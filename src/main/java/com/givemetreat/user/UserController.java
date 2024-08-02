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
}
