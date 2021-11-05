package com.eaugusto.theguildedrose.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eaugusto.theguildedrose.model.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	
}