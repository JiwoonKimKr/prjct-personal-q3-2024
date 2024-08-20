package com.givemetreat.admin;

import java.util.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.api.utils.PrivateKeysAdminPage;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminRestController {
	private final PrivateKeysAdminPage privateKeysAdminPage;
	
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam String loginId
			, @RequestParam String password
			, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		if(loginId.equals(privateKeysAdminPage.loginIdAdmin) == false
				|| password.equals(privateKeysAdminPage.passwordAdmin) == false ) {
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
