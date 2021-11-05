package com.eaugusto.theguildedrose.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.eaugusto.theguildedrose.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	
	Page<Item> findByNameContainingIgnoreCaseOrderByNameAsc(@Param("name") String name, Pageable pageable);
	
}