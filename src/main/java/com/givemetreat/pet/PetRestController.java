package com.givemetreat.pet;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.givemetreat.pet.bo.PetBO;
import com.givemetreat.pet.domain.Pet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Tag(name = "Pet RestController", description = "[Client] Pet RestAPI Controller 반려견 RestAPI 관련 컨트롤러")
@RequestMapping("/pet")
@RequiredArgsConstructor
@RestController
public class PetRestController {
	private final PetBO petBO;

	@Operation(summary = "addPetInfo() 반려견 등록", description = "해당 사용자가 반려견을 등록")
	@Parameters({
		@Parameter(name = "<String> name", description = "<RequestPart> 반려견 이름", example = "망고")
		, @Parameter(name = "<String> age", description = "<RequestPart> 반려견 연령대 ('under6months', 'adult', 'senior')", example = "under6months")
		, @Parameter(name = "<MultipartFile> imageProfile", description = "<RequestPart> 반려견 이미지", example = "manggo-puppy.img")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"등록 가능 프로필 갯수가 초과하였습니다. 최대 5개의 반려견 프로필을 등록할 수 있습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"기존 이름과 중복됩니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"해당 반려견 정보를 추가하지 못 하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/register-pet")
	public Map<String, Object> addPetInfo(
			@RequestPart("name") String name
			, @RequestPart String age //TODO enum 타입으로 변형하는 것을 고려해야
			, @RequestPart(name = "imageProfile", required = false) MultipartFile file
			, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		int userId = (int) session.getAttribute("userId");
		String loginId = (String) session.getAttribute("loginId");
		
		//반려견 등록 횟수 5회 이하로 제한; 반려견 프로필 6개 이상 넘어가지 않도록
		Integer countPetsRegistered = petBO.countPetsByUserId(userId);
		
		if(countPetsRegistered >= 5) {
			result.put("code", 500);
			result.put("error_message", "등록 가능 프로필 갯수가 초과하였습니다. 최대 5개의 반려견 프로필을 등록할 수 있습니다.");
			return result;
		}
		
		//반려견 이름 중복 확인
		Pet petCur = petBO.getPetByUserIdAndName(userId, name);
		
		if(petCur != null) {
			result.put("code", 500);
			result.put("error_message", "기존 이름과 중복됩니다");
			return result;
		}
		
		int count = petBO.addPet(userId, loginId, name, age, file);
		
		if(count < 1) {
			result.put("code", 500);
			result.put("error_message", "해당 반려견 정보를 추가하지 못 하였습니다.");
			return result;
		}
		result.put("code", 200);
		result.put("result", "success");
		return result;
	}
	
	@Operation(summary = "updatePetInfo() 반려견 정보 수정", description = "해당 반려견 정보를 수정")
	@Parameters({
		@Parameter(name = "<int> petId", description = "<RequestPart> 반려견 PK", example = "3")
		, @Parameter(name = "<String> name", description = "<RequestPart> 반려견 이름", example = "망고")
		, @Parameter(name = "<String> age", description = "<RequestPart> 반려견 연령대 ('under6months', 'adult', 'senior')", example = "under6months")
		, @Parameter(name = "<MultipartFile> imageProfile", description = "<RequestPart> 반려견 이미지", example = "manggo-puppy.img")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"등록 가능 프로필 갯수가 초과하였습니다. 최대 5개의 반려견 프로필을 등록할 수 있습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"해당 반려견 정보가 존재하지 않습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"기존 이름과 중복됩니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"해당 반려견 정보를 수정하지 못하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/update-pet")
	public Map<String, Object> updatePetInfo(
			@RequestParam int petId
			, @RequestPart("name") String name
			, @RequestPart String age //TODO enum 타입으로 변형하는 것을 고려해야
			, @RequestPart String hasImageChanged //TODO enum 타입으로 변형하는 것을 고려해야
			, @RequestPart(name = "imageProfile", required = false) MultipartFile file
			, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		int userId = (int) session.getAttribute("userId");
		String loginId = (String) session.getAttribute("loginId");
		
		//반려견 등록 횟수 5회 이하로 제한; 반려견 프로필 6개 이상 넘어가지 않도록
		Integer countPetsRegistered = petBO.countPetsByUserId(userId);
		
		if(countPetsRegistered >= 5) {
			result.put("code", 500);
			result.put("error_message", "등록 가능 프로필 갯수가 초과하였습니다. 최대 5개의 반려견 프로필을 등록할 수 있습니다.");
			return result;
		}
		
		Pet pet = petBO.getPetByUserIdAndPetId(userId, petId);
		
		if(ObjectUtils.isEmpty(pet)) {
			result.put("code", 500);
			result.put("error_message", "해당 반려견 정보가 존재하지 않습니다.");
			return result;
		}
		
		Boolean imageChanged = hasImageChanged.equals("true") ? true : false;
		
		pet = petBO.updatePet(userId, loginId, petId, name, age, imageChanged, file);
		
		if(ObjectUtils.isEmpty(pet)) {
			result.put("code", 500);
			result.put("error_message", "해당 반려견 정보를 수정하지 못 하였습니다.");
			return result;
		}
		result.put("code", 200);
		result.put("result", "success");
		return result;
	}
	
	@Operation(summary = "deletePetInfo() 반려견 정보 삭제", description = "해당 반려견 정보 삭제")
	@Parameters({
		@Parameter(name = "<int> petId", description = "반려견 PK", example = "3")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"해당 반려견 정보가 존재하지 않습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500", description = "error_message: \"해당 반려견 정보를 삭제하지 못하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/delete-pet")
	public Map<String, Object> deletePetInfo(@RequestParam int petId
											, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		int userId = (int) session.getAttribute("userId");
		
		Pet pet = petBO.getPetByUserIdAndPetId(userId, petId);
		
		if(ObjectUtils.isEmpty(pet)) {
			result.put("code", 500);
			result.put("error_message", "해당 반려견 정보가 존재하지 않습니다.");
			return result;
		}
		
		int count = petBO.deletePetByUserIdAndPetId(userId, petId);
		
		if(count < 1) {
			result.put("code", 500);
			result.put("error_message", "해당 반려견 정보가 삭제하지 못 하였습니다. 관리자에게 문의하십시오.");
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "success");
		return result;
	}
}
