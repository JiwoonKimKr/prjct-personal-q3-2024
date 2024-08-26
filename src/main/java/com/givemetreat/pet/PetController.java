package com.givemetreat.pet;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

@Tag(name = "Pet Controller", description = "[Client] Pet Controller 반려견 페이지 관련 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/pet")
@Controller
public class PetController {
	private final PetBO petBO;
	
	@Operation(summary = "petListView() 반려견 조회 페이지 ", description = "해당 사용자 관련 반려견 조회 페이지")
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
	
	@Operation(summary = "registerPetView() 반려견 등록 페이지", description = "해당 사용자 반려견 등록 페이지")
	@ApiResponse(responseCode = "200"
				, description = "/pet/registerPet.html")
	@GetMapping("/register-pet-view")
	public String registerPetView() {
		
		return "pet/registerPet";
	}
	
	@Operation(summary = "updatePetView() 반려견 정보 수정", description = "반려견 정보 수정 페이지")
	@Parameters({
		@Parameter(name = "<int> petId", description = "해당 반려견(pet) PK")
		,@Parameter(name = "<HttpSession> session", description = "session")
		, @Parameter(name = "<Model> model", description = "MVC Model")
	})
	@ApiResponse(responseCode = "200"
				, description = "/pet/updatePet.html"
								+ "<br> Model Attributes"
								+ "<br> &lt;Pet&gt; pet "
				, content = @Content(mediaType = "TEXT_HTML", schema = @Schema(implementation = Pet.class))								)
	@GetMapping("/update-pet-view")
	public String updatePetView(@RequestParam int petId
								, HttpSession session
								, Model model) {
		int userId = (int) session.getAttribute("userId");
		Pet pet = petBO.getPetByUserIdAndPetId(userId, petId);
		model.addAttribute("pet", pet);
		return "pet/updatePet";
	}
}
