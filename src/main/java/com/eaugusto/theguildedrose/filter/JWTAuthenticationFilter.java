package com.eaugusto.theguildedrose.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.eaugusto.theguildedrose.config.AuthenticationProperties;
import com.eaugusto.theguildedrose.model.ApiUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final AuthenticationProperties authenticationProperties;

	@Override 
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			ApiUser creds = new ObjectMapper()
					.readValue(request.getInputStream(), ApiUser.class);

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getUsername(),
							creds.getPassword(),
							new ArrayList<>())
					);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override 
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
		String token = JWT.create()
				.withSubject(((User) auth.getPrincipal()).getUsername())
				.withClaim("role", auth.getAuthorities().iterator().next().getAuthority())
				.withExpiresAt(new Date(System.currentTimeMillis() + authenticationProperties.getExpirationTime()))
				.sign(Algorithm.HMAC512(authenticationProperties.getJwtSecret().getBytes()));

		//START - SENDING JWT AS A BODY
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(
				"{\"" + authenticationProperties.getJwtTokenField() + "\":\"" + authenticationProperties.getJwtTokenPrefix() + " " + token + "\"}"
				);
		//END - SENDING JWT AS A BODY

		//START - SENDING JWT AS A HEADER
		response.addHeader(authenticationProperties.getJwtTokenField(), authenticationProperties.getJwtTokenPrefix() + " " + token);
		//END - SENDING JWT AS A HEADER
	}
}
