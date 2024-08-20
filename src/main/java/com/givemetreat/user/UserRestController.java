package com.givemetreat.user;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.givemetreat.common.EncryptUtils;
import com.givemetreat.common.validation.WordingValidation;
import com.givemetreat.user.bo.UserBO;
import com.givemetreat.user.bo.UserEmailBO;
import com.givemetreat.user.domain.UserEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserRestController {
	private final UserBO userBO;
	private final UserEmailBO userEmailBO;
	
	//localhost/user/email-verification
	/**
	 * 사용자 이메일(로그인아이디) 확인 후 해당 이메일로 인증코드 보내기;
	 * @param userEmail
	 * @return Map<String, Object>
	 */
	@PostMapping("/email-verification")
	public Map<String, Object> emailVerification(@RequestParam String userEmail){
		Map<String, Object> result = new HashMap<>();

		LocalDateTime timeRequested = LocalDateTime.now();
		userEmailBO.SendSimpeVerificationMail(userEmail, timeRequested);

		
		result.put("code", 200);
		result.put("result", "success");
		
		return result;
	}
	
	@PostMapping("/verificationi-code")
	public Map<String, Object> verificationCode(@RequestParam String request){
		Map<String, Object> result = new HashMap<>();

		LocalDateTime timeRequested = LocalDateTime.now();
		userEmailBO.verifyCode(request, timeRequested);
		
		result.put("code", 200);
		result.put("result", "success");
		
		return result;
	} 
	
	
	//localhost/user/sign-in
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam String loginId
			, @RequestParam String password
			, HttpSession session){
		
		Map<String, Object> result = new HashMap<>();

		UserEntity user = userBO.getUserByLoginId(loginId);
		
		if(user == null) {
			result.put("code", 500);
			result.put("error_message", "아이디를 확인해 주세요.");
			return result;
		}
		
		String msg = user.getSalt();
		
		String hashedPassword = EncryptUtils.sha256(msg ,password);
		
		if(hashedPassword.equals(user.getPassword()) == false) {
			result.put("code", 500);
			result.put("error_message", "비밀번호를 확인해 주세요.");
			return result;
		}
		session.setAttribute("userId", user.getId());
		session.setAttribute("loginId", user.getLoginId());
		session.setAttribute("userName", user.getNickname());
		
		result.put("code", 200);
		result.put("result", "success");
		
		return result;
	}

	//localhost/user/sign-up
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam String loginId
			, @RequestParam String password
			, @RequestParam String passwordConfirm
			, @RequestParam String nickname
			, HttpSession session){
		
		Map<String, Object> result = new HashMap<>();
		
		//서버쪽에서도 거치는 Validation 과정
		//아이디
		if(WordingValidation.isEmail(loginId) == false) {
			result.put("code", 500);
			result.put("error_message", "올바른 이메일 주소를 입력하여 주십시오.");
			return result;
		}
		//비밀번호
		if(WordingValidation.hasAlphaLowerUpperNumericSpecialLengthBetween8And16(password) == false
			|| WordingValidation.hasAlphaLowerUpperNumericSpecialLengthBetween8And16(passwordConfirm) == false
			) {
			result.put("code", 500);
			result.put("error_message", "비밀번호는 8~16자의 영어 대소문자, 숫자, 특수문자로 구성되어야 합니다.");
			return result;			
		}
		if(password.equals(passwordConfirm) == false) {
			result.put("code", 500);
			result.put("error_message", "비밀번호가 일치하지 않습니다.");
			return result;
		}
		

		UserEntity userHasExist = userBO.getUserByLoginId(loginId);
		//로그인 아이디(이메일) 중복 체크
		if(userHasExist != null) {
			result.put("code", 500);
			result.put("error_message", "해당 이메일은 이미 가입하였습니다.");
			return result;
		}
		
		//비밀번호 중복 체크-Server-side Validation
		if(password.equals(passwordConfirm) == false) {
			result.put("code", 500);
			result.put("error_message", "비밀번호가 중복되었습니다.");
			return result;
		}
		
		String salt = EncryptUtils.getSalt();
		
		String hashedPassword = EncryptUtils.sha256(salt, password);
		
		UserEntity user = userBO.addUser(loginId, hashedPassword, salt, nickname);
		
		if(user != null) {
			result.put("code", 200);
			result.put("result", "success");
		} else {
			result.put("code", 500);
			result.put("error_message", "회원가입에 실패하였습니다. 관리자에게 문의하시길 바랍니다.");
		}
		
		return result;
	}
	
	
	
}
