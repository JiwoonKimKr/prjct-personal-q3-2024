package com.givemetreat.user;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.givemetreat.common.utils.EncryptUtils;
import com.givemetreat.common.validation.WordingValidation;
import com.givemetreat.user.bo.UserBO;
import com.givemetreat.user.bo.UserEmailBO;
import com.givemetreat.user.domain.UserEntity;
import com.givemetreat.user.domain.VerificationCodeEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "User RestController", description = "[Client] User RestAPI Controller")
@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserRestController {
	private final UserBO userBO;
	private final UserEmailBO userEmailBO;
	
	
	@Operation(summary = "resetPassword() 비밀번호 재설정", description = "비밀번호 재설정.")
	@Parameters({
		@Parameter(name = "<String> loginId", description = "로그인 아이디", example = "asdf")
		, @Parameter(name = "<String> password", description = "비밀번호")
		, @Parameter(name = "<String> passwordConfirm", description = "비밀번호 확인용")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500__1", description = "error_message: \"올바른 이메일 주소를 입력하여 주십시오.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__2", description = "error_message: \"비밀번호는 8~16자의 영어 대소문자, 숫자, 특수문자로 구성되어야 합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__3", description = "error_message: \"비밀번호가 일치하지 않습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__4", description = "error_message: \"새로운 비밀번호를 입력하는 시도가 실패하였습니다. 관리자에게 문의하시길 바랍니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/reset-password")
	public Map<String, Object> resetPassword(
			@RequestParam String loginId
			, @RequestParam String password
			, @RequestParam String passwordConfirm){
		
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

		UserEntity user = userBO.getUserByLoginId(loginId);
		
		String salt = EncryptUtils.getSalt();
		
		String hashedPassword = EncryptUtils.sha256(salt, password);
		
		user = userBO.updatePassword(user, hashedPassword, salt);
		
		if(ObjectUtils.isEmpty(user)) {
			result.put("code", 500);
			result.put("error_message", "새로운 비밀번호를 입력하는 시도가 실패하였습니다. 관리자에게 문의하시길 바랍니다.");
		}
		log.info("[UserRestController] new password get saved.");
		
		if(user != null) {
			result.put("code", 200);
			result.put("result", "success");
		}
		
		return result;
	}
	
	
	/**
	 * 사용자 이메일(로그인아이디) 확인 후 해당 이메일로 인증코드 보내기;
	 * @param userEmail
	 * @return Map<String, Object>
	 */
	@Operation(summary = "email Verification 이메일 코드 발송", description = "사용자가 입력한 이메일(아이디)로 인증 코드를 발송함.")
	@Parameters({
		@Parameter(name = "<String> userEmail", description = "이메일(아이디)", example = "example@exampleGivemeTreat.com")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"해당 이메일에 인증코드를 보내지 못 하였습니다. 관리자에게 문의하시길 바랍니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})	
	@PostMapping("/email-verification")
	public Map<String, Object> emailVerification(@RequestParam String userEmail){
		Map<String, Object> result = new HashMap<>();

		LocalDateTime timeRequested = LocalDateTime.now();
		Boolean hasEmailSent = userEmailBO.sendVerificationMailWithTemplate(userEmail, timeRequested);

		if(hasEmailSent == false || ObjectUtils.isEmpty(hasEmailSent)) {
			result.put("code", 500);
			result.put("error_message", "해당 이메일에 인증코드를 보내지 못 하였습니다. 관리자에게 문의하시길 바랍니다.");
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "success");
		
		return result;
	}
	
	@Operation(summary = "verification Code() 인증코드 검증", description = "사용자가 이메일(아이디)로 받은 인증 코드를 입력한 후, 검증 절차를 거친다.")
	@Parameters({
		@Parameter(name = "<String> codeRequested", description = "사용자가 입력한 코드")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"코드 인증이 실패하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/verification-code")
	public Map<String, Object> verificationCode(@RequestParam String codeRequested){
		Map<String, Object> result = new HashMap<>();

		LocalDateTime timeRequested = LocalDateTime.now();
		VerificationCodeEntity codeEntity = userEmailBO.verifyCode(codeRequested, timeRequested);
		
		if(ObjectUtils.isEmpty(codeEntity)) {
			result.put("code", 500);
			result.put("error_message", "코드 인증이 실패하였습니다.");
			return result;
		}
		
		String loginId = codeEntity.getLoginId();
		
		result.put("code", 200);
		result.put("result", "success");
		result.put("loginId", loginId);
		
		return result;
	}
	
	@Operation(summary = "verifyLoginId() 이메일 검증", description = "사용자가 입력한 이메일(아이디)가 기존 DB에 존재하는지 검증한다.")
	@Parameters({
		@Parameter(name = "<String> emailTyped", description = "사용자가 입력한 이메일")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"해당 이메일이 존재하지 않습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/verify-loginId")
	public Map<String, Object> verifyLoginId(@RequestParam String emailTyped){
		Map<String, Object> result = new HashMap<>();

		UserEntity user = userBO.getUserByLoginId(emailTyped);
		
		if(ObjectUtils.isEmpty(user)) {
			result.put("code", 500);
			result.put("error_message", "해당 이메일이 존재하지 않습니다.");
			return result;
		}
		
		result.put("code", 200);
		result.put("result", "success");
		result.put("email", user.getLoginId());
		
		return result;
	}
	
	@Operation(summary = "signIn() 로그인", description = "로그인")
	@Parameters({
		@Parameter(name = "<String> loginId", description = "로그인 아이디(이메일)")
		, @Parameter(name = "<String> password", description = "비밀번호")
		, @Parameter(name = "<HttpSession> session", description = "session 세션")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500__1", description = "error_message: \"아이디를 확인해 주세요.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__2", description = "error_message: \"비밀번호를 확인해 주세요.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\", <br> 로그인 성공 시 session에 &lt;Integer&gt; \"userId\", &lt;String&gt; \"loginId\", &lt;Integer&gt; \"userName\" 세 값 추가", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(@RequestParam String loginId
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
		
		//Session에 값 추가
		session.setAttribute("userId", user.getId());
		session.setAttribute("loginId", user.getLoginId());
		session.setAttribute("userName", user.getNickname());
		session.setAttribute("imageProfile", user.getImgProfile());
		session.setAttribute("selfDescription", user.getSelfDesc());
		
		result.put("code", 200);
		result.put("result", "success");
		
		return result;
	}

	@Operation(summary = "signUp() 회원가입", description = "회원가입")
	@Parameters({
		@Parameter(name = "<String> loginId", description = "로그인 아이디(이메일)")
		, @Parameter(name = "<String> password", description = "비밀번호")
		, @Parameter(name = "<String> passwordConfirm", description = "비밀번호 확인용")
		, @Parameter(name = "<HttpSession> session", description = "session 세션")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500__1", description = "error_message: \"올바른 이메일 주소를 입력하여 주십시오.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__2", description = "error_message: \"비밀번호는 8~16자의 영어 대소문자, 숫자, 특수문자로 구성되어야 합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__3", description = "error_message: \"비밀번호가 일치하지 않습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__4", description = "error_message: \"해당 이메일은 이미 가입하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__5", description = "error_message: \"회원가입에 실패하였습니다. 관리자에게 문의하시길 바랍니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\"", content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam String loginId
			, @RequestParam String password
			, @RequestParam String passwordConfirm
			, @RequestParam String nickname){
		
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
		
		String salt = EncryptUtils.getSalt();
		
		String hashedPassword = EncryptUtils.sha256(salt, password);
		
		UserEntity user = userBO.addUser(loginId, hashedPassword, salt, nickname);
		
		if(ObjectUtils.isEmpty(user) == false) {
			result.put("code", 200);
			result.put("result", "success");
		} else {
			result.put("code", 500);
			result.put("error_message", "회원가입에 실패하였습니다. 관리자에게 문의하시길 바랍니다.");
		}
		
		return result;
	}
	
	@Operation(summary = "updateImageProfile() 프로필이미지 추가", description = "사용자 프로필이미지 추가")
	@Parameters({
		@Parameter(name = "<MultipartFile> imageProfile", description = "[RequestPart]<MultipartFile> file")
		, @Parameter(name = "<HttpSession> session", description = "session 세션")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500", description = "error_message: \"프로필 이미지를 등록하지 못 하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\" <br> session에 이미지 경로인 &lt;String&gt; 'imageProfile' 추가 "
			, content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/register-image-profile")
	public Map<String, Object> updateImageProfile(
			@RequestPart(value = "imageProfile") MultipartFile file
			, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		int userId = (int) session.getAttribute("userId");
		String loginId = (String) session.getAttribute("loginId");
		
		UserEntity user = userBO.updateImageProfile(userId, loginId, file);
		
		if(ObjectUtils.isEmpty(user.getImgProfile()) 
				|| ObjectUtils.isEmpty(user.getImgThumbnail())) {
			result.put("code", 500);
			result.put("error_message", "프로필 이미지를 등록하지 못 하였습니다.");
			return result;
		}
		
		session.setAttribute("imageProfile", user.getImgProfile());
		
		result.put("code", 200);
		result.put("result", "success");
		return result;
	}
	
	@Operation(summary = "updateSelfDescription() 자기소개 추가", description = "사용자 자기소개 추가")
	@Parameters({
		@Parameter(name = "<String> selfDescription", description = "자기소개", example = "육식왕을 모시는 butler라고 합니다.")
		, @Parameter(name = "<HttpSession> session", description = "session 세션")
	})
	@ApiResponses({
		@ApiResponse(responseCode = "500__1", description = "error_message: \"문자열만 입력 가능합니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "500__2", description = "error_message: \"자기소개를 등록하지 못 하였습니다.\"", content = @Content(mediaType = "APPLICATION_JSON"))
		, @ApiResponse(responseCode = "200", description = "result: \"success\" <br> session에 &lt;String&gt; 'selfDescription' 추가"
			, content = @Content(mediaType = "APPLICATION_JSON"))
	})
	@PostMapping("/register-self-description")
	public Map<String, Object> updateSelfDescription(
									@RequestParam String selfDescription
									, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		
		if(WordingValidation.hasLiteralsOnly(selfDescription) == false) {
			result.put("code", 500);
			result.put("error_message", "문자열만 입력 가능합니다.");
			return result;
		}
		
		int userId = (int) session.getAttribute("userId");
		UserEntity user = userBO.updateSelfDescription(userId, selfDescription.trim());
		
		if(ObjectUtils.isEmpty(user.getSelfDesc())
				|| selfDescription.trim().equals(user.getSelfDesc()) == false) {
			result.put("code", 500);
			result.put("error_message", "자기소개를 등록하지 못 하였습니다.");
			return result;
		}
		
		session.setAttribute("selfDescription", user.getSelfDesc());
		
		result.put("code", 200);
		result.put("result", "success");
		return result;
	}
}
