package com.prashant.junit.testing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prashant.junit.testing.dto.AuthenticationRequest;
import com.prashant.junit.testing.dto.AuthenticationResponse;
import com.prashant.junit.testing.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
		log.debug("Login attempt for user: {}" ,request.getEmail());
		return ResponseEntity.ok(authService.login(request));
	}
}
