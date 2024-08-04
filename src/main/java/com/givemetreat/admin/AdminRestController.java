package com.givemetreat.admin;

import java.util.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/admin")
@RestController
public class AdminRestController {

	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam String loginId
			, @RequestParam String password
			, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		if(loginId.equals("admin") == false || password.equals("admin") == false ) {
			result.put("code", 500);
			result.put("error_message", "관리자 페이지 로그인에 실패하였습니다.");
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "success");
		
		session.setAttribute("authorizationCurrent", "admin");
		
		return result;
	}
}
