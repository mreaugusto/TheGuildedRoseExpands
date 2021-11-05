package com.eaugusto.theguildedrose.model.response;

import com.eaugusto.theguildedrose.model.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateResponse {
    private Item item;
	private Long quantity;
}