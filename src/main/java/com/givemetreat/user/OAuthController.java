package com.givemetreat.user;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import com.givemetreat.api.dto.ResponseTokenKakaoApi;
import com.givemetreat.api.dto.UserInfoKakaoApi;
import com.givemetreat.api.utils.PrivateKeysKakaoApi;
import com.givemetreat.common.EncryptUtils;
import com.givemetreat.user.bo.UserBO;
import com.givemetreat.user.domain.UserEntity;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class OAuthController {
	private final UserBO userBO;

	@SuppressWarnings("unchecked")
	@GetMapping("/OAuth/kakao-sign-up")
	public String redirectedFromKakaoSignUp(
			@RequestParam(required = false) String code
			,@RequestParam(required = false) String error
			,@RequestParam(required = false) String error_description
			, HttpSession session
			){
		log.info("[OAuth: Kakao Api: 1st] code for Token:{}", code);
		
		//사용자가 카카오 관련정보 동의하지 않음; 회원가입 리롤
		if(error != null && error.equals("access_denied")) {
			return "redirect:/";
		}
		
		//2)Token as Response
		ResponseTokenKakaoApi TokenResponse = postRequestWithCodeForToken(code);
		if(TokenResponse == null) {
			return "redirect:/";
		}
		
		//3)AccessToken
		String accessToken = TokenResponse.getAccess_token();
		UserInfoKakaoApi userInfo = accessUserInfoWithAccessToken(accessToken);
		if(userInfo == null) {
			return "redirect:/";
		}
		
		String idUserInfo = userInfo.getId();
		String email = (String) userInfo.getKakao_account().get("email");
		Map<String, Object> profile = (Map<String, Object>) userInfo.getKakao_account().get("profile");
		String nickname  = (String) profile.get("nickname");
		
		if(idUserInfo == null || email == null || nickname == null) {
			return "redirect:/";
		}
		
		//이미 이메일이 DB에 존재하는 경우, 비밀번호 일치하는지 확인해야
		UserEntity userCur = userBO.getUserByLoginId(email);
		if(userCur != null) {
			//salt 생성하고 userInfo의 id값을 합쳐서 비밀번호를 생성
			String msc = userCur.getSalt();
			String hashedPassword = EncryptUtils.sha256(msc, idUserInfo);
			
			if(hashedPassword.equals(userCur.getPassword())) {
				//비밀번호 일치 -> 로그인 진행
				session.setAttribute("userId", userCur.getId());
				session.setAttribute("loginId", userCur.getLoginId());
				session.setAttribute("userName", userCur.getNickname());
				log.info("[OAuth: Kakao Api: User validated for Sign-In] 3 attributes are set in HttpSession.");
				return "redirect:/product/product-list-view";
			} else {
				//비밀번호 불칠이 -> 로그인 실패;
				return "redirect:/";
			}
		}
		
		String salt = EncryptUtils.getSalt();
		String password = EncryptUtils.sha256(salt, idUserInfo);
		
		UserEntity user = userBO.addUser(email, password, salt, nickname);
		
		if(user == null) {
			return "redirect:/";
		}
		
		session.setAttribute("userId", user.getId());
		session.setAttribute("loginId", user.getLoginId());
		session.setAttribute("userName", user.getNickname());
		return "redirect: localhost/product/productList";
	}
	
	private ResponseTokenKakaoApi postRequestWithCodeForToken(String code){
		//Post Request from SPRING!
		
		//body
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", PrivateKeysKakaoApi.GRANT_TYPE);
		formData.add("client_id", PrivateKeysKakaoApi.CLIENT_ID);
		formData.add("redirect_uri", PrivateKeysKakaoApi.REDIRECT_URI);
		formData.add("code", code);
		formData.add("client_secret", PrivateKeysKakaoApi.CLIENT_SECRET);
		
		//Post Request Trial
		WebClient webClient = WebClient.builder()
										.baseUrl( PrivateKeysKakaoApi.URL_TOKEN_KAKAO_OAUTH)
										.defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
										.build();
		ResponseTokenKakaoApi response = webClient.post()
							.bodyValue(formData)
							.retrieve()
							.bodyToMono(ResponseTokenKakaoApi.class)
							.block();
		if(response == null) {
			return null;
		}
		
		log.info("[OAuth: Kakao Api: 2nd] token Received as DTO:{}", response);
		return response;
	}
	
	private UserInfoKakaoApi accessUserInfoWithAccessToken(String accessToken) {
		WebClient webClient = WebClient.builder().build();
		UserInfoKakaoApi userInfo = webClient.get()
				.uri(PrivateKeysKakaoApi.URL_ACCESS_KAKAO_OAUTH)
				.header("Authorization", "Bearer " + accessToken)
				.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
				.retrieve()
				.bodyToMono(UserInfoKakaoApi.class)
				.block();
		if(userInfo == null) {
			return null;
		}
		
		log.info("[OAuth: Kakao Api: 3rd] Response As User Info:{}", userInfo);
		return userInfo;
	}
	
}
