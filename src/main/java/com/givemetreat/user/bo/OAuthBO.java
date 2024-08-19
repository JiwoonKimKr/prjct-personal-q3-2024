package com.givemetreat.user.bo;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.givemetreat.api.dto.ResponseTokenKakaoApi;
import com.givemetreat.api.dto.UserInfoKakaoApi;
import com.givemetreat.api.utils.PrivateKeysKakaoApi;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OAuthBO {

	@Transactional
	public ResponseTokenKakaoApi postRequestWithCodeForToken(String code){
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
	
	@Transactional
	public UserInfoKakaoApi accessUserInfoWithAccessToken(String accessToken) {
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
