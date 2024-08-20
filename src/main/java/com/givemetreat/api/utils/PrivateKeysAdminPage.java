package com.givemetreat.api.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "private-keys-admin-page")
public class PrivateKeysAdminPage {
	public String loginIdAdmin;
	public String passwordAdmin;
}
