package com.ecc.company.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecc.company.domain.LastUpdate;
import com.ecc.company.repository.LastUpdateRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
	
	private LastUpdateRepository lastUpdateRepo;
	
	public Optional<LastUpdate> getLastUpdate() {
		
		return lastUpdateRepo.findFirstByOrderByLastUpdateDesc();
		
	}
	
}
