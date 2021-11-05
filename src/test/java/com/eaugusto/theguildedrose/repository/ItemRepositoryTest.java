package com.eaugusto.theguildedrose.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.eaugusto.theguildedrose.model.Item;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ItemRepositoryTest {

	@Autowired
    private ItemRepository itemRepository;

	@BeforeEach
    public void setUp() {
    }
	
	@Test
	void testCanSearchWhenRepositoryIsEmpty() {
		assertTrue(itemRepository.findAll().isEmpty());
	}
	
	@Test
	void testCanFindPersistedItem() {
		Item item = Item.builder().name("Test").description("Description").price(1).build();
		Long persistedId = itemRepository.save(item).getId();
		
		assertEquals("Test", itemRepository.findById(persistedId).get().getName());
	}
	
	@Test
	void testCannotCreateItemWithPriceZero() {
		Item item = Item.builder().name("Test").description("Description").price(0).build();
		assertThrows(ConstraintViolationException.class, () -> itemRepository.save(item).getId());
	}
	
	@Test
	void testCanFilterItemsByPartsOfName() {
		Item item1 = Item.builder().name("Test1").description("Description").price(1).build();
		itemRepository.save(item1).getId();
		
		Item item2 = Item.builder().name("Test2").description("Description").price(1).build();
		itemRepository.save(item2).getId();
		
		Page<Item> page = itemRepository.findByNameContainingIgnoreCaseOrderByNameAsc("1", PageRequest.of(0, 10));
		assertEquals("Test1", page.getContent().get(0).getName());
		
	}

	@AfterEach
    public void tearDown() {
    }
	
}