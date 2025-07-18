package com.prashant.junit.testing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.prashant.junit.testing.security.JwtAuthFilter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        		 .csrf(csrf -> csrf.disable())
        		 .authorizeHttpRequests(authorize -> 
        		 	authorize.requestMatchers("/auth/**","/student/**").permitAll()
        		 	//.requestMatchers("/teacher/create").permitAll()
        		 .anyRequest().authenticated())
				 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				 .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

