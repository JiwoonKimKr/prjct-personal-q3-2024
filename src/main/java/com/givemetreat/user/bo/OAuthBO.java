package com.givemetreat.user.bo;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.givemetreat.api.dto.ResponseTokenKakaoApi;
import com.givemetreat.api.dto.UserInfoKakaoApi;
import com.givemetreat.api.utils.PrivateKeysKakaoApi;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthBO {
	private final PrivateKeysKakaoApi privateKeysKakaoApi;

	@Transactional
	public ResponseTokenKakaoApi postRequestWithCodeForToken(String code){
		//Post Request from SPRING!
		
		//body
		
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", privateKeysKakaoApi.grant_type);
		formData.add("client_id", privateKeysKakaoApi.client_id);
		formData.add("redirect_uri", privateKeysKakaoApi.redirect_uri);
		formData.add("code", code);
		formData.add("client_secret", privateKeysKakaoApi.client_secret);
		
		//Post Request Trial
		WebClient webClient = WebClient.builder()
										.baseUrl( privateKeysKakaoApi.url_token)
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
				.uri(privateKeysKakaoApi.url_access)
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
