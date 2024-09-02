package com.givemetreat.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "file-location-config")
public class FileLocationConfig {
	public String FILE_UPLOAD_PATH;
}