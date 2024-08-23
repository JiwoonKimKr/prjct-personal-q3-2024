package com.givemetreat.admin;

import java.util.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.api.utils.PrivateKeysAdminPage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
@Tag(name = "Admin Rest RestController", description = "[Admin] Admin RestAPI Controller; 관리자 메인 페이지 관련 RestAPI 컨트롤러")
@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminRestController {
	private final PrivateKeysAdminPage privateKeysAdminPage;
	
	@Operation(summary = "signIn() 관리자페이지 로그인", description = "관리자 페이지 로그인")
	@Parameters({
		@Parameter(name = "<String> loginId", description = "관리자 페이지 로그인 아이디")
		, @Parameter(name = "<String> password", description = "관리자 페이지 비밀번호")
		, @Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"관리자 페이지 로그인에 실패하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\" <br> session에 \"authorizationCurrent\"key 부여", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam String loginId
			, @RequestParam String password
			, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		if(loginId.equals(privateKeysAdminPage.loginIdAdmin) == false
				|| password.equals(privateKeysAdminPage.passwordAdmin) == false ) {
			result.put("code", 500);
			result.put("error_message", "관리자 페이지 로그인에 실패하였습니다.");
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "success");
		
		session.setAttribute("authorizationCurrent", "admin");
		
		return result;
	}
}
