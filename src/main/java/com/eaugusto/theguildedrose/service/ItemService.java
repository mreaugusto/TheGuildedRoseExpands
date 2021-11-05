package com.eaugusto.theguildedrose.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codahale.metrics.SlidingTimeWindowArrayReservoir;
import com.eaugusto.theguildedrose.config.SurgePriceProperties;
import com.eaugusto.theguildedrose.model.Item;
import com.eaugusto.theguildedrose.model.Warehouse;
import com.eaugusto.theguildedrose.repository.ItemRepository;

@Service
public class ItemService {

	private Map<Long, SlidingTimeWindowArrayReservoir> viewMap;

	private final ItemRepository itemRepository;
	private final WarehouseService warehouseService;
	private final SurgePriceProperties surgePriceProperties;

	public ItemService(ItemRepository itemRepository, WarehouseService warehouseService, SurgePriceProperties surgePriceProperties) {
		this.itemRepository = itemRepository;
		this.warehouseService = warehouseService;
		this.surgePriceProperties = surgePriceProperties;
		viewMap = new HashMap<>();
	}

	public Page<Item> listInventory(String name, Pageable pageable) {
		if(name == null || name.isEmpty()) {
			return itemRepository.findAll(pageable);
		} else {
			return itemRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name, pageable);
		}
	}

	public Optional<Item> findById(Long id) {
		return itemRepository.findById(id);
	}

	public Item save(Item item) {
		Item savedItem = calculatePrice(itemRepository.save(item));
		
		Optional<Warehouse> warehouse = warehouseService.findById(item.getId());
		if(!warehouse.isPresent()) {
			Warehouse newWarehouse = Warehouse.builder().id(item.getId()).available(0).item(item).build();
			warehouseService.save(newWarehouse);
		}
		
		return savedItem;
	}

	public Item calculatePrice(Item item) {
		Item copiedItem = new Item();
		BeanUtils.copyProperties(item, copiedItem);
		
		SlidingTimeWindowArrayReservoir slidingTimeWindowArrayReservoir = getSlidingTimeWindow(item);
		
		if(slidingTimeWindowArrayReservoir.size() >= surgePriceProperties.getViewThreshold()) {
			copiedItem.setPrice(item.getPrice() * (1 + (surgePriceProperties.getPriceIncreasePercent() / 100)));
		}
		
		return copiedItem;
	}

	public SlidingTimeWindowArrayReservoir viewItem(Item item) {
		SlidingTimeWindowArrayReservoir slidingTimeWindowArrayReservoir = getSlidingTimeWindow(item);
		slidingTimeWindowArrayReservoir.update(1);
		return slidingTimeWindowArrayReservoir;
	}

	private SlidingTimeWindowArrayReservoir getSlidingTimeWindow(Item item) {
		viewMap.computeIfAbsent(item.getId(), (id) -> new SlidingTimeWindowArrayReservoir(surgePriceProperties.getTimeWindownInSeconds(), TimeUnit.SECONDS));
		SlidingTimeWindowArrayReservoir slidingTimeWindowArrayReservoir = viewMap.get(item.getId());
		return slidingTimeWindowArrayReservoir;
	}
	
	public Item viewAndCalculateItem(Item item) {
		Item copiedItem = new Item();
		BeanUtils.copyProperties(item, copiedItem);
		
		SlidingTimeWindowArrayReservoir slidingTimeWindowArrayReservoir = viewItem(item);
		
		if(slidingTimeWindowArrayReservoir.size() >= 10) {
			copiedItem.setPrice(item.getPrice() * 1.1);
		}
		
		return copiedItem;
	}

}
