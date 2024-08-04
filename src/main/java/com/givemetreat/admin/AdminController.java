package com.givemetreat.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
	@GetMapping("/admin/sign-in-view")
	public String AdminSignInView() {
		return "admin/adminSignIn";
	}
	
	@GetMapping("/admin/admin-main-view")
	public String mainView() {
		return "admin/adminMain"; 
	}
	
	@GetMapping("/admin/sign-out")
	public String signOut(HttpSession session) {
		session.removeAttribute("authorizationCurrent");
		return "redirect:/admin/sign-in-view";
	}
}
