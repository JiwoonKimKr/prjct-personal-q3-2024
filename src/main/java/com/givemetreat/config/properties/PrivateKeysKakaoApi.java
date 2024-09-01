package com.givemetreat.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix="private-keys-kakao-api")
public class PrivateKeysKakaoApi {
	public String url_token;
	
	public String grant_type;
	public String client_id;
	public String redirect_uri;
	public String client_secret;
	
	public String url_access;
}
