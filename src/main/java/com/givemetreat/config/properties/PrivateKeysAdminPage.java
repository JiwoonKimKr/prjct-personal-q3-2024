package com.givemetreat.config.properties;

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
