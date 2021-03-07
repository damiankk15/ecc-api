package com.ecc;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ecc.company.model.Selenium;
import com.ecc.company.model.Source;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "ecc.company-service")
public class ApplicationProperties {
	
	private Map<String, Selenium> selenium;
	private Map<String, Source> sources;
	
}
