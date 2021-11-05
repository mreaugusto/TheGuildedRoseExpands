package com.eaugusto.theguildedrose.resource;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eaugusto.theguildedrose.model.Warehouse;
import com.eaugusto.theguildedrose.service.WarehouseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/warehouse")
public class WarehouseResource {

	private final WarehouseService warehouseService;

	@PutMapping("/{itemId}/add/{quantity}")
	public Warehouse addItemsToWarehouse(
			@PathVariable(name = "itemId", required = true) Long itemId, 
			@PathVariable(name = "quantity", required = true) Long quantity, HttpServletRequest request) {
		
		if(quantity <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Quantity should be a positive number");
		}
		
		Warehouse warehouse = warehouseService.findById(itemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item not found"));
		
		warehouse.setAvailable(warehouse.getAvailable() + quantity);

		Warehouse updatedWarehouse = warehouseService.save(warehouse);
		
		log.info("Updated warehouse: {}", updatedWarehouse);
		
		return updatedWarehouse;
		
	}

}