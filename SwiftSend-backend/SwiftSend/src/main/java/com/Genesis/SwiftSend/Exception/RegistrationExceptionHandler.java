package com.Genesis.SwiftSend.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author rohan
 *
 */
@RestControllerAdvice
public class RegistrationExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return errors;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<Object> userNotFound(UserAlreadyExistsException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", ex.getMessage());
		response.put("httpStatus", ex.getHttpStatus());
		response.put("errorDetails", ex.getErrorDetails());
		response.put("timestamp", LocalDateTime.now());
		return new ResponseEntity<>(response, ex.getHttpStatus());
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Object> handleCustomException(CustomException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", ex.getMessage());
		response.put("httpStatus", ex.getHttpStatus());
		response.put("errorDetails", ex.getErrorDetails());
		response.put("timestamp", LocalDateTime.now());
		return new ResponseEntity<>(response, ex.getHttpStatus());
	}
}
