package com.eaugusto.theguildedrose.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.eaugusto.theguildedrose.model.request.UserCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockWebServer;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "app.authentication.jwtSecret=123ABC"
})
public class LoginIntegrationTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;

	private static MockWebServer mockWebServer;

	@BeforeAll
	static void setupMockWebServer() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
	}

	@Test
	void testGenerateAccessToken() {
		
		UserCreateRequest user = UserCreateRequest.builder().username("admin").password("passwd").role("ADMIN").build();
		HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserCreateRequest> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity("http://localhost:" + port + "/public/user", request, String.class);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		
        HttpEntity<String> requestLogin = new HttpEntity<>("{ \"username\": \"admin\", \"password\": \"passwd\" }", headers);
        ResponseEntity<String> resultLogin = this.restTemplate.postForEntity("http://localhost:" + port + "/login", requestLogin, String.class);
		assertEquals(HttpStatus.OK, resultLogin.getStatusCode());
		assertTrue(resultLogin.getHeaders().containsKey("Authorization"));
		
	}
	
	public static String asJsonString(final Object obj){
		try{
			return new ObjectMapper().writeValueAsString(obj);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}

}
