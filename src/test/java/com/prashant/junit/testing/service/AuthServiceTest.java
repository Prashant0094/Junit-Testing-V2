package com.prashant.junit.testing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.prashant.junit.testing.dto.AuthenticationRequest;
import com.prashant.junit.testing.dto.AuthenticationResponse;
import com.prashant.junit.testing.jwt.JwtService;
import com.prashant.junit.testing.security.CustomUserDetailsService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
	
	@InjectMocks 
	AuthService authService;
	
	@Mock
	CustomUserDetailsService detailsService;
	
	@Mock
	JwtService jwtService;
	
	@Mock 
	AuthenticationManager authManager;
	
	@Test
	void login_authenticateAndReturnJwt_whenUserNamePasswordExistAndMatch() {
		AuthenticationRequest response = new AuthenticationRequest("Any@email.com","password");
		
		UserDetails mockUser = mock(UserDetails.class);
		when(mockUser.getUsername()).thenReturn("Any@email.com");
		
		when(detailsService.loadUserByUsername("Any@email.com")).thenReturn(mockUser);
		when(jwtService.generateToken("Any@email.com")).thenReturn("JWT-generated-token");
		
		AuthenticationResponse result = authService.login(response);
		
		assertNotNull(result);
		assertEquals("JWT-generated-token",result.getToken());
		
		verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(detailsService).loadUserByUsername("Any@email.com");
		verify(jwtService).generateToken("Any@email.com");
	}
}
