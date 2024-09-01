package com.givemetreat.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "server-side-location-for-file-manager-service")
public class ServerSideFileLocationConfig {
	public String FILE_UPLOAD_PATH;
}