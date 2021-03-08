package com.ecc.security.repository;

import java.util.Optional;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecc.security.model.RoleEnum;
import com.ecc.security.model.Role;

@Repository
//public interface RoleRepository extends JpaRepository<Role, Long> {
public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Optional<Role> findByName(RoleEnum name);
	
}
