package com.eaugusto.theguildedrose.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eaugusto.theguildedrose.model.ApiUser;

public interface UserRepository extends JpaRepository<ApiUser, Long> {
	
    Optional<ApiUser> findByUsername(String username);
    
}