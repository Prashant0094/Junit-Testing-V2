package com.prashant.junit.testing.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import com.prashant.junit.testing.jwt.JwtService;

@ExtendWith(MockitoExtension.class)
public class JwtSeviceTest {

	@InjectMocks
	private JwtService jwtService;
	@Mock
	private UserDetails userDetails;

	@BeforeEach
	void setup() {
		ReflectionTestUtils.setField(jwtService, "secret", "12345678901234567890123456789012");
		ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1000*60*60);
	}

	@Test
	void generateTokenShouldGenerateToken() {
		String email = "Amy@email.com";
		String token = jwtService.generateToken(email);

		assertNotNull(token);
		System.out.println("Generated Token: " + token);
	}
	@Test
	void extractUsernameShouldExtractUsername() {
		String email = "Amy@email.com";
		String token = jwtService.generateToken(email);
		String extracted = jwtService.extractUsername(token);

		assertEquals(extracted, email);
	}
}
