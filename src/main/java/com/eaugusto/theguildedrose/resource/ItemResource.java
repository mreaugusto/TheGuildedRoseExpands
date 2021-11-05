package com.eaugusto.theguildedrose.resource;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eaugusto.theguildedrose.model.Item;
import com.eaugusto.theguildedrose.service.ItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RestController
@RequiredArgsConstructor
public class ItemResource {

	private final ItemService itemService;

	@PostMapping("/admin/item")
	public ResponseEntity<Item> addItem(@Valid @RequestBody Item item, HttpServletRequest request) {
		Item createdItem = itemService.save(item);
		log.info("Item created: {}", createdItem);
		return ResponseEntity.created(URI.create(ServletUriComponentsBuilder
				.fromRequestUri(request)
				.path("/" + createdItem.getId().toString()).toUriString()))
				.body(createdItem);
	}

	@PutMapping("/admin/item")
	public Item updateItem(@Valid @RequestBody Item item) {
		itemService.findById(item.getId())
		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Not Found"));

		Item updatedItem = itemService.save(item);
		log.info("Item updated: {}", updatedItem);
		return updatedItem;
	}
	
	@GetMapping("/public/item")
    public Page<Item> listInventory(@RequestParam(name = "name", required = false) String name, Pageable pageable) {
    	return itemService.listInventory(name, pageable)
    			.map(itemService::viewAndCalculateItem);
    }

}