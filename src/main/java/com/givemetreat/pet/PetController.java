package com.givemetreat.pet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/pet")
@Controller
public class PetController {

	@GetMapping("/pet-list-view")
	public String petListView() {
		
		return "pet/petList";
	}
	@GetMapping("/register-pet-view")
	public String registerPetView() {
		
		return "pet/registerPet";
	}
}
