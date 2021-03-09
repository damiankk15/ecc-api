package com.ecc.company.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ecc.company.domain.LastUpdate;
import com.ecc.company.service.CompanyService;
import com.ecc.security.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/company")
public class CompanyServiceController {
	
	@Autowired
	private CompanyService companyService;
	
	@GetMapping("/getLastUpdate")
	public ResponseEntity<LastUpdate> getLastUpdate(Authentication authentication) {

		Optional<LastUpdate> optLastUpdate = companyService.getLastUpdate();
		
		if (optLastUpdate.isPresent()) {
			return new ResponseEntity<>(optLastUpdate.get(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		
	}
	
	@GetMapping("/updateCompaniesList/{companyMarket}")
	public ResponseEntity<SseEmitter> updateCompaniesList(
			@PathVariable("companyMarket") String companyMarket,
			Authentication authentication) {
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		return new ResponseEntity<>(companyService.updateCompaniesList(companyMarket, userDetails.getUsername()), HttpStatus.OK);
		
	}
	
}
