package com.eaugusto.theguildedrose.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter 
@Setter
@ConfigurationProperties("app.surge-price")
public class SurgePriceProperties {
	
	long timeWindownInSeconds;
	
	long viewThreshold;
	
	double priceIncreasePercent;
}