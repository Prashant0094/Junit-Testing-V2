package com.prashant.junit.testing.dto;

public class AuthenticationResponse {

	private String token;
	
	public AuthenticationResponse() {}

	public AuthenticationResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
