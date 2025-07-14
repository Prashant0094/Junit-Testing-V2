package com.prashant.junit.testing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.prashant.junit.testing.dto.AuthenticationRequest;
import com.prashant.junit.testing.dto.AuthenticationResponse;
import com.prashant.junit.testing.jwt.JwtService;
import com.prashant.junit.testing.security.CustomUserDetailsService;

@Service
public class AuthService {

 @Autowired
 private AuthenticationManager authenticationManager;

 @Autowired
 private CustomUserDetailsService userDetailsService;

 @Autowired
 private JwtService jwtService;
 
 private static final Logger log = LoggerFactory.getLogger(AuthService.class);

 public AuthenticationResponse login(AuthenticationRequest request) {
     authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(
                     request.getEmail(),
                     request.getPassword())
     );

     UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());
     String token = jwtService.generateToken(user.getUsername());
     log.info("Token generated for User with email: {} " , request.getEmail());
     return new AuthenticationResponse(token);
 }
}

