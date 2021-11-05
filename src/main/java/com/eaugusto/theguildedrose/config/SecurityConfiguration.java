package com.eaugusto.theguildedrose.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.eaugusto.theguildedrose.filter.JWTAuthenticationFilter;
import com.eaugusto.theguildedrose.filter.JWTAuthorizationFilter;
import com.eaugusto.theguildedrose.service.AuthenticationUserDetailService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final AuthenticationUserDetailService authenticationUserDetailService;
	private final AuthenticationProperties authenticationProperties;

	@Override protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
		.antMatchers("/public/**").permitAll()

		//ROLE BASED AUTHENTICATION START
        .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
		//ROLE BASED AUTHENTICATION END
		
        .anyRequest().authenticated()
		.and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager(), authenticationProperties))
		.addFilter(new JWTAuthorizationFilter(authenticationManager(), authenticationProperties))
		// this disables session creation on Spring Security
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationUserDetailService).passwordEncoder(bCryptPasswordEncoder);
	}
}
