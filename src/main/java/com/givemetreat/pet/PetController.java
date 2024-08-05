package com.givemetreat.pet;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.givemetreat.pet.bo.PetBO;
import com.givemetreat.pet.domain.Pet;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/pet")
@Controller
public class PetController {
	private final PetBO petBO;
	
	@GetMapping("/pet-list-view")
	public String petListView(
			HttpSession session
			,Model model) {
		
		int userId = (int) session.getAttribute("userId");
		List<Pet> listPets = petBO.getPetsByUserId(userId);
		model.addAttribute("listPets", listPets);
		
		return "pet/petList";
	}
	@GetMapping("/register-pet-view")
	public String registerPetView() {
		
		return "pet/registerPet";
	}
}
