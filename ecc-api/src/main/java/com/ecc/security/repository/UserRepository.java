package com.ecc.security.repository;

import java.util.Optional;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecc.security.model.User;

@Repository
//public interface UserRepository extends JpaRepository <User, Long> {
public interface UserRepository extends CrudRepository <User, Long> {
	
	Optional<User> findByUsername(String username);
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);
	
}
