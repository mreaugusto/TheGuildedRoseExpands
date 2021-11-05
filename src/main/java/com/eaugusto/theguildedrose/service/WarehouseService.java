package com.eaugusto.theguildedrose.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.eaugusto.theguildedrose.model.Warehouse;
import com.eaugusto.theguildedrose.repository.WarehouseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WarehouseService {

	private final WarehouseRepository warehouseRepository;

	public Optional<Warehouse> findById(Long id) {
		return warehouseRepository.findById(id);
	}
	
	public Warehouse save(Warehouse warehouse) {
		return warehouseRepository.save(warehouse);
	}
	
	
	
}
