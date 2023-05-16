package com.rni.mes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "token")
@EnableConfigurationProperties
public record JwtTokenParam(
		long shirtAccessToken,
	    long longAccessToken,
	    long refreshToken
		) {

}
