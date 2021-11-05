package com.eaugusto.theguildedrose.resource;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.eaugusto.theguildedrose.model.request.UserCreateRequest;
import com.eaugusto.theguildedrose.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/public/user")
@RequiredArgsConstructor
public class UserResource {
	
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
    	try {
			userService.createUser(userCreateRequest);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
        return ResponseEntity.ok().build();
    }

}