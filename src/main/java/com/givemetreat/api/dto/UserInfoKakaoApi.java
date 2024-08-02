package com.givemetreat.api.dto;

import java.util.*;

import lombok.Data;

@Data
public class UserInfoKakaoApi {
	String id;
	String connected_At;
	Map<String, Object> properties;
	Map<String, Object> kakao_account;
}
	/*	
		{"id":3648558758
		,"connected_at":"2024-08-02T14:39:27Z"
		,"properties":{"nickname":"nuij"}
		,"kakao_account":
			{"profile_nickname_needs_agreement":false
			,"profile_image_needs_agreement":true
			,"profile": 
				{"nickname":"nuij"
				,"is_default_nickname":false}
			,"has_email":true
			,"email_needs_agreement":false
			,"is_email_valid":true
			,"is_email_verified":true
			,"email":"syjnk@daum.net"}
		}
	*/

