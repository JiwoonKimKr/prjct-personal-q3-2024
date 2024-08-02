package com.givemetreat.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.givemetreat.api.dto.ResponseTokenKakaoApi;
import com.givemetreat.api.dto.UserInfoKakaoApi;
import com.givemetreat.api.utils.PrivateKeysKakaoApi;
import com.givemetreat.common.EncryptUtils;
import com.givemetreat.user.bo.UserBO;
import com.givemetreat.user.domain.UserEntity;

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
			){
		log.info("[OAuth: Kakao Api: 1st] code for Token:{}", code);
		
		if(error != null && error.equals("access_denied")) {
			return "redirect: localhost/";
		}
		
		//2)Token as Response
		ResponseTokenKakaoApi TokenResponse = postRequestWithCodeForToken(code);
		if(TokenResponse == null) {
			return "redirect: localhost/";
		}
		
		//3)AccessToken
		String accessToken = TokenResponse.getAccess_token();
		
		UserInfoKakaoApi userInfo = accessUserInfoWithAccessToken(accessToken);
		
		log.info("[OAuth: Kakao Api: 3rd] Response As User Info:{}", userInfo);
		
		String idUserInfo = userInfo.getId();
		String email = (String) userInfo.getKakao_account().get("email");
		Map<String, Object> profile = (Map<String, Object>) userInfo.getKakao_account().get("profile");
		String nickname  = (String) profile.get("nickname");
		
		if(idUserInfo == null || email == null || nickname == null) {
			return "redirect: localhost/";
		}
		
		//이미 이메일이 DB에 존재하는 경우, 비밀번호 일치하는지 확인해야
		UserEntity userCur = userBO.getUserByLoginId(email);
		if(userCur != null) {
			MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
			formData.add("loginId", email);
			formData.add("password", idUserInfo);
			WebClient webClient = WebClient.builder().build();
			String response = webClient.post()
							.bodyValue(formData)
							.retrieve()
							.bodyToMono(String.class)
							.block();
			
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> mapResponse = new HashMap<>();
			try {
				mapResponse = objectMapper.readValue(response, Map.class);
			} catch (JsonMappingException e) {
				return "redirect: localhost/";
			} catch (JsonProcessingException e) {
				return "redirect: localhost/";
			}
			
			if((Integer) mapResponse.get("code") == 200) {
				return "redirect: /product/product-list-view";
			} else {
				return "redirect: localhost/";
			}
			
		}
		
		String salt = EncryptUtils.getSalt();
		String password = EncryptUtils.sha256(salt, idUserInfo);
		
		UserEntity user = userBO.addUser(email, password, salt, nickname);
		
		if(user == null) {
			return "redirect: localhost/";
		}
		
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
		return userInfo;
	}
	
}
