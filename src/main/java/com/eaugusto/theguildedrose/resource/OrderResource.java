package com.eaugusto.theguildedrose.resource;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eaugusto.theguildedrose.model.Item;
import com.eaugusto.theguildedrose.model.Warehouse;
import com.eaugusto.theguildedrose.model.request.OrderCreateRequest;
import com.eaugusto.theguildedrose.model.response.OrderCreateResponse;
import com.eaugusto.theguildedrose.service.ItemService;
import com.eaugusto.theguildedrose.service.WarehouseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user/order")
public class OrderResource {

	private final ItemService itemService;
	
	private final WarehouseService warehouseService;
	

	@PostMapping
	public OrderCreateResponse placeOrder(@Valid @RequestBody OrderCreateRequest order, HttpServletRequest request) {

		Item item = itemService.findById(order.getItemId()).map(itemService::viewAndCalculateItem)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		Warehouse warehouse = warehouseService.findById(order.getItemId()).orElseThrow();
		if(warehouse.getAvailable() - order.getQuantity() < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient item units available to order");
		}
		
		warehouse.setAvailable(warehouse.getAvailable() - order.getQuantity());

		Warehouse updatedWarehouse = warehouseService.save(warehouse);
		
		log.info("Updated warehouse: {}", updatedWarehouse);
		
		return OrderCreateResponse.builder().item(item).quantity(order.getQuantity()).build();
		
	}

}