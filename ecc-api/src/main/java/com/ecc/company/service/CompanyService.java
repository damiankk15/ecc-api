package com.ecc.company.service;

import java.util.Optional;

import com.ecc.company.domain.LastUpdate;

public interface CompanyService {
	
	public Optional<LastUpdate> getLastUpdate();
	
}
