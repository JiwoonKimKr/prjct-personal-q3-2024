package com.givemetreat.user;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import com.givemetreat.api.dto.ResponseTokenKakaoApi;
import com.givemetreat.api.dto.UserInfoKakaoApi;
import com.givemetreat.api.utils.PrivateKeysKakaoApi;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class OAuthController {

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
		
		Integer id = Integer.parseInt(userInfo.getId());
		String email = (String) userInfo.getKakao_account().get("email");
		@SuppressWarnings("unchecked")
		Map<String, Object> profile = (Map<String, Object>) userInfo.getKakao_account().get("profile");
		String nickname  = (String) profile.get("nickname");
		
		if(id == null || email == null || nickname == null) {
			return "redirect: localhost/";
		}
		
		
		
		return "redirect: /product/productList";
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
