package com.givemetreat.user;

import java.util.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.user.bo.AdminUserBO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Admin User RestController", description = "[Admin] User RestAPI Controller")
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@RestController
public class AdminUserRestController {
	private final AdminUserBO adminUserBO;
	
	@Operation(summary = "사용자 등록 반려견 삭제", description = "등록된 반려견 삭제.")
	@Parameters({
		@Parameter(name = "<int> userId", description = "사용자 PK", example = "10")
		, @Parameter(name = "<int> petId", description = "사용자 해당 반려견 PK", example = "1")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"회원가입에 실패하였습니다. 관리자에게 문의하시길 바랍니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
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
