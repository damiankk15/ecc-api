package com.ecc.company.service;

import java.util.Optional;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ecc.company.domain.LastUpdate;

public interface CompanyService {
	
	public Optional<LastUpdate> getLastUpdate();
	
	public SseEmitter updateCompaniesList(String companyMarket, String modUser);
	
}
