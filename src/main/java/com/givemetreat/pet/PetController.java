package com.givemetreat.pet;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.givemetreat.pet.bo.PetBO;
import com.givemetreat.pet.domain.Pet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Tag(name = "Pet Controller", description = "[Client] Pet Controller")
@RequiredArgsConstructor
@RequestMapping("/pet")
@Controller
public class PetController {
	private final PetBO petBO;
	
	@Operation(summary = "petListView", description = "해당 사용자 관련 반려견 조회 페이지")
	@Parameters({
			@Parameter(name = "<HttpSession> session", description = "session")
			, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponse(responseCode = "200"
				, description = "/pet/petList.html"
								+ "<br> Model Attributes"
								+ "<br> List &lt;Pet&gt; listPets "
				, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = Pet.class)))
	@GetMapping("/pet-list-view")
	public String petListView(
			HttpSession session
			,Model model) {
		
		int userId = (int) session.getAttribute("userId");
		List<Pet> listPets = petBO.getPetsByUserId(userId);
		model.addAttribute("listPets", listPets);
		
		return "pet/petList";
	}
	
	@Operation(summary = "registerPetView", description = "해당 사용자 반려견 등록 페이지")
	@ApiResponse(responseCode = "200"
				, description = "/pet/registerPet.html <br> Model Attribute: \"listUsers\" ")
	@GetMapping("/register-pet-view")
	public String registerPetView() {
		
		return "pet/registerPet";
	}
}
