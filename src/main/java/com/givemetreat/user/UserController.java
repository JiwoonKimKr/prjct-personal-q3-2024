package com.givemetreat.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.givemetreat.api.utils.PrivateKeysKakaoApi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Tag(name = "User Controller", description = "[Client] User Controller")
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
	private final PrivateKeysKakaoApi privateKeysKakaoApi;

	@Operation(summary = "signInView", description = "로그인 페이지")
	@ApiResponse(responseCode = "200", description = "\"user/signIn.html\"", content = @Content(mediaType = "TEXT_HTML"))
	@GetMapping("/sign-in-view")
	public String signInView() {
		return "user/signIn";
	}	
	
	@Operation(summary = "signUpView", description = "회원가입 페이지")
	@ApiResponse(responseCode = "200", description = "\"user/signUp.html\"", content = @Content(mediaType = "TEXT_HTML"))
	@GetMapping("/sign-up-view")
	public String signUpView() {
		return "user/signUp";
	}
	
	@Operation(summary = "kakaoApi", description = "카카오 OAuth API 버튼이 눌렸을 경우 리다이렉트")
	@ApiResponse(responseCode = "200", description = "\"redirect:https://kauth.kakao.com/oauth/authorize?___\" 관련값을 통해 카카오 API로 리다이렉트")	
	@GetMapping("/kakao-api")
	public String kakaoApi(){
		return "redirect:https://kauth.kakao.com/oauth/authorize?" 
				+ "response_type=code"
				+ "&client_id=" + privateKeysKakaoApi.client_id
				+ "&redirect_uri=http://localhost/OAuth/kakao-sign-up";
	}
	
	@Operation(summary = "signOut", description = "로그아웃; Get메소드와 Post메소드 둘 다 허용")
	@Parameters({
		@Parameter(name = "<HttpSession> session", description = "session")
	})
	@ApiResponse(responseCode = "200", description = "\"redirect:/user/sign-in-view\"")
	@RequestMapping(path = "/sign-out", method = {RequestMethod.GET, RequestMethod.POST})
	public String signOut(HttpSession session) {
		session.removeAttribute("loginId");
		session.removeAttribute("userId");
		session.removeAttribute("userName");
		
		return "redirect:/user/sign-in-view";
	}
	
	@Operation(summary = "findEmailView", description = "이메일(아이디) 찾기 페이지")
	@ApiResponse(responseCode = "200", description = "\"user/findEmail.html\"", content = @Content(mediaType = "TEXT_HTML"))
	@GetMapping("/find-email-view")
	public String findEmailView() {
		return  "user/findEmail";
	}
	
	@Operation(summary = "verifyCodeView", description = "이메일로 송신된 코드 확인 페이지")
	@ApiResponse(responseCode = "200", description = "\"user/verifyCode.html\"", content = @Content(mediaType = "TEXT_HTML"))
	@GetMapping("/verify-code-view")
	public String verifyCodeView() {
		return "user/verifyCode";
	}
	
	@Operation(summary = "resetPasswordView", description = "비밀번호 새로 입력")
	@ApiResponse(responseCode = "200", description = "\"user/resetPassword.html\"", content = @Content(mediaType = "TEXT_HTML"))
	@GetMapping("/reset-password-view")
	public String resetPasswordView() {
		return "user/resetPassword";
	}
}
