package com.prashant.junit.testing.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String,String>> handleResourceNotFound(ResourceNotFoundException ex){
		Map <String,String> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now().toString());
		error.put("error", ex.getMessage());
		error.put("status", "NOT_FOUND");
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String,String>> handleException(Exception ex){
		Map <String, String> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now().toString());
		error.put("error", ex.getMessage());
		error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.name());
		return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler (IllegalArgumentException.class)
	public ResponseEntity<Map<String,String>> handleIllegalArgumentException(IllegalArgumentException ex){
		Map <String,String> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now().toString());
		error.put("error", ex.getMessage());
		error.put("status", HttpStatus.BAD_REQUEST.name());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
			errors.put(error.getField(), error.getDefaultMessage())
		);
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

}

