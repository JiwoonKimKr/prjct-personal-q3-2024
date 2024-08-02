package com.givemetreat.pet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/pet")
@Controller
public class PetController {

	@GetMapping("/pet-list-view")
	public String petListView(
			HttpSession session) {
		
		return "pet/petList";
	}
}
