package com.givemetreat.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@Tag(name = "Admin Controller", description = "[Admin] Admin Controller; 관리자 메인페이지 관련 컨트롤러")
@Controller
public class AdminController {
	
	@Operation(summary = "AdminSignInView() 관리자 페이지 로그인 화면", description = "관리자 페이지 로그인 화면")
	@ApiResponse(responseCode = "200"
		, description = "admin/adminSignIn.html"
		, content = @Content(mediaType = "TEXT_HTML"))
	@GetMapping("/admin/sign-in-view")
	public String AdminSignInView() {
		return "admin/adminSignIn";
	}
	
	@Operation(summary = "mainView() 관리자 페이지 메인 페이지", description = "관리자 페이지 메인 페이지")
	@ApiResponse(responseCode = "200"
		, description = "admin/adminMain.html"
		, content = @Content(mediaType = "TEXT_HTML"))
	@GetMapping("/admin/admin-main-view")
	public String mainView() {
		return "admin/adminMain"; 
	}
	
	@Operation(summary = "signOut() 로그아웃", description = "로그아웃")
	@Parameters({
		@Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponse(responseCode = "200"
		, description = "redirect:/admin/sign-in-view <br> session에서 \"authorizationCurrent\"key:value 제거")
	@GetMapping("/admin/sign-out")
	public String signOut(HttpSession session) {
		session.removeAttribute("authorizationCurrent");
		return "redirect:/admin/sign-in-view";
	}
}
