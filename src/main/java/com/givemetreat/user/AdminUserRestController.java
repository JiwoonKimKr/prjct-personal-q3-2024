package com.givemetreat.user;

import java.util.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.user.bo.AdminUserBO;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/user")
@RequiredArgsConstructor
@RestController
public class AdminUserRestController {
	private final AdminUserBO adminUserBO;
	
	@DeleteMapping("/delete-user-pet-current")
	public Map<String, Object> deleteUserPetCurrent(
									@RequestParam int userId
									, @RequestParam int petId){
		
		int count = adminUserBO.deletePetByUserIdAndPetId(userId, petId);
		
		Map<String, Object> result = new HashMap<>();
		if(count < 1) {
			result.put("code", 500);
			result.put("error_message", "회원가입에 실패하였습니다. 관리자에게 문의하시길 바랍니다.");
		}
		
		result.put("code", 200);
		result.put("result", "success");
		
		return result;
	}
}
