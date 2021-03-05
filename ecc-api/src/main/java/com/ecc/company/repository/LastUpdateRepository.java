package com.ecc.company.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ecc.company.domain.LastUpdate;

public interface LastUpdateRepository extends CrudRepository<LastUpdate, Integer> {
	
	Optional<LastUpdate> findFirstByOrderByLastUpdateDesc();
	
}
