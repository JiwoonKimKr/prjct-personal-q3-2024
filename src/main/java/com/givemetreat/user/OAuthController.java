package com.givemetreat.user;

import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import com.givemetreat.api.dto.ResponseTokenKakaoApi;
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
		ResponseTokenKakaoApi response = postRequestWithCodeForToken(code);
		if(response == null) {
			return "redirect: localhost/";
		}
		
		//3)AccessToken
		String accessToken = response.getAccess_token();
		
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
										.baseUrl("https://kauth.kakao.com/oauth/token")
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
}
