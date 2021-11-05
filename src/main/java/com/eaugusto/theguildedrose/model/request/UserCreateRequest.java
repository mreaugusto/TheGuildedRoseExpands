package com.eaugusto.theguildedrose.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
	
	@NotEmpty(message = "Please provide the username")
    private String username;
	
	@NotEmpty(message = "Please provide the password")
    private String password;
	
	@NotEmpty(message = "Please provide the role")
	@Pattern(regexp = "USER|ADMIN", message = "Role should be USER or ADMIN")
    private String role;
}
