package com.ecc.company.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecc.company.domain.LastUpdate;
import com.ecc.company.service.CompanyService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/company", produces = "application/json")
public class CompanyServiceController {
	
	@Autowired
	private CompanyService companyService;
	
	@GetMapping("/getLastUpdate")
	public ResponseEntity<LastUpdate> getLastUpdate() {
		
		Optional<LastUpdate> optLastUpdate = companyService.getLastUpdate();
		
		if (optLastUpdate.isPresent()) {
			return new ResponseEntity<>(optLastUpdate.get(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		
	}
	
}
