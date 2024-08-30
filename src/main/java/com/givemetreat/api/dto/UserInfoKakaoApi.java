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

