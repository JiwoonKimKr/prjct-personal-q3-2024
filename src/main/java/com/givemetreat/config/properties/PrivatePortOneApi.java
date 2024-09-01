package com.givemetreat.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix="private-port-one-api")
public class PrivatePortOneApi {

	public String storeId;
	public String channelKey;
	
}
