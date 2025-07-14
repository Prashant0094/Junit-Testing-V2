package com.prashant.junit.testing.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prashant.junit.testing.dto.AuthenticationRequest;
import com.prashant.junit.testing.dto.AuthenticationResponse;
import com.prashant.junit.testing.jwt.JwtService;
import com.prashant.junit.testing.security.JwtAuthFilter;
import com.prashant.junit.testing.service.AuthService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

	@MockBean
	private AuthService authService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private JwtAuthFilter jwtAuthFilter;

	@MockBean
	private JwtService jwtService;

	@Test
	void loginShouldLoginUserWhenAuthenticationComplete() throws Exception {
		AuthenticationRequest request = new AuthenticationRequest("Ghost@email.com", "Pass");
		AuthenticationResponse response = new AuthenticationResponse("jwt-token");
		when(authService.login(any(AuthenticationRequest.class))).thenReturn(response);
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
				.andExpect(jsonPath("$.token").value("jwt-token"))
				.andDo(result -> System.out.println("RESPONSE BODY: " + result.getResponse().getContentAsString()));
	}

	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
