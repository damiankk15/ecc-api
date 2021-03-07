package com.ecc.company.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecc.company.domain.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Integer> {
	
	Optional<Company> findFirstByIsin(String isin);
	
}
