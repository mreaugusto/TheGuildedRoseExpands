package com.eaugusto.theguildedrose.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eaugusto.theguildedrose.config.AuthenticationProperties;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private AuthenticationProperties authenticationProperties;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationProperties authenticationProperties) {
        super(authenticationManager);
		this.authenticationProperties = authenticationProperties;
    }

    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(authenticationProperties.getJwtTokenField());

        if (header == null || !header.startsWith(authenticationProperties.getJwtTokenPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(authenticationProperties.getJwtTokenField());
        if (token != null) {
            // parse the token.
            DecodedJWT verify = JWT.require(Algorithm.HMAC512(authenticationProperties.getJwtSecret().getBytes()))
                .build()
                .verify(token.replace(authenticationProperties.getJwtTokenPrefix() + " ", ""));

            String username = verify.getSubject();
            String role = verify.getClaim("role").asString();

            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null, getAuthorities(role));
            }
            return null;
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

}
