package com.ecc.company.service;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ecc.company.domain.LastUpdate;
import com.ecc.company.model.SseEvent;
import com.ecc.company.repository.LastUpdateRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
	
	private LastUpdateRepository lastUpdateRepo;
	
	public Optional<LastUpdate> getLastUpdate() {
		
		return lastUpdateRepo.findFirstByOrderByLastUpdateDesc();
		
	}
	
	public SseEmitter updateCompaniesList(String companyMarket, String modUser) {
		
		SseEmitter emitter = new SseEmitter(100000l);
		SseEvent event = new SseEvent(emitter);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		
		
		return emitter;
		
	}
	
}
