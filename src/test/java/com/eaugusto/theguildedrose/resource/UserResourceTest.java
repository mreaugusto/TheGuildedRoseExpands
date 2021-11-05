package com.eaugusto.theguildedrose.resource;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eaugusto.theguildedrose.model.request.UserCreateRequest;
import com.eaugusto.theguildedrose.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class UserResourceTest {

	@InjectMocks
	UserResource userResource;

	@Mock
	UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
	}

	@Test
	void testCreatesUser() throws Exception {
		UserCreateRequest userCreateRequest = new UserCreateRequest("user", "password", "USER");
		mockMvc.perform(post("/public/user").
				contentType(MediaType.APPLICATION_JSON).
				content(asJsonString(userCreateRequest))).
		andExpect(status().isOk());

		verify(userService).createUser(userCreateRequest);
	}
	
	@Test
	void testCreatesUserWithRoleAdmin() throws Exception {
		UserCreateRequest userCreateRequest = new UserCreateRequest("user", "password", "ADMIN");
		mockMvc.perform(post("/public/user").
				contentType(MediaType.APPLICATION_JSON).
				content(asJsonString(userCreateRequest))).
		andExpect(status().isOk());

		verify(userService).createUser(userCreateRequest);
	}
	
	@Test
	void testDontCreateUserOtherThanUserOrAdmin() throws Exception {
		UserCreateRequest userCreateRequest = new UserCreateRequest("user", "password", "ADMIN2");
		mockMvc.perform(post("/public/user").
				contentType(MediaType.APPLICATION_JSON).
				content(asJsonString(userCreateRequest))).
		andExpect(status().isBadRequest());

		verify(userService, never()).createUser(userCreateRequest);
	}

	public static String asJsonString(final Object obj){
		try{
			return new ObjectMapper().writeValueAsString(obj);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

}