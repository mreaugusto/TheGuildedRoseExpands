package com.eaugusto.theguildedrose.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter 
@Setter
@ConfigurationProperties("app.authentication")
public class AuthenticationProperties {
	
	String signUpUrl;
	
	String jwtSecret;
	
	long expirationTime;
	
	String jwtTokenPrefix;
	
	String jwtTokenField;
}