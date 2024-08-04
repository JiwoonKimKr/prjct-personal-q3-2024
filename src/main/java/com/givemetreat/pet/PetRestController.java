package com.givemetreat.pet;

import java.util.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.givemetreat.pet.bo.PetBO;
import com.givemetreat.pet.domain.Pet;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequestMapping("/pet")
@RequiredArgsConstructor
@RestController
public class PetRestController {
	private final PetBO petBO;

	@PostMapping("/register-pet")
	public Map<String, Object> addPetInfo(
			@RequestPart("name") String name
			, @RequestParam int age //TODO enum 타입으로 변형하는 것을 고려해야
			, @RequestPart(name = "imageProfile", required = false) MultipartFile file
			, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		int userId = (int) session.getAttribute("userId");
		String loginId = (String) session.getAttribute("loginId");
		
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
		}
		result.put("code", 200);
		result.put("result", "success");
		return result;
	}
}
