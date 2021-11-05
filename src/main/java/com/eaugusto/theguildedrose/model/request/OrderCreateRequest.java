package com.eaugusto.theguildedrose.model.request;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.Min;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Data
public class OrderCreateRequest {
	
	@Min(value = 1, message = "Item id should be a positive number")
    private Long itemId;
	
	@Min(value = 1, message = "Quantity should be a positive number")
	private Long quantity;

	@CreatedDate
    @Column(name = "created_date")
    Date creationDateTime;
    
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
}
