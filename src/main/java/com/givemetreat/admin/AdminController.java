package com.givemetreat.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	@GetMapping("/admin")
	public String adminView() {
		return "redirect:http://localhost/admin/sign-in-view";
	}
	
	@GetMapping("/admin/sign-in-view")
	public String AdminSignInView() {
		return "admin/adminSignIn";
	}
	
	@GetMapping("/admin/admin-main-view")
	public String mainView() {
		return "admin/adminMain"; 
	}
}
