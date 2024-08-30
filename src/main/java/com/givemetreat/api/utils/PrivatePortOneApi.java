package com.givemetreat.api.utils;

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
