package com.Genesis.SwiftSend.Exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author rohan
 *
 */
@RestControllerAdvice
public class RegistrationExceptionHandler {

	private final HttpServletRequest httpServletRequest;
	private final Logger logger = LoggerFactory.getLogger(RegistrationExceptionHandler.class);

	public RegistrationExceptionHandler(HttpServletRequest httpServletRequest) {

		this.httpServletRequest = httpServletRequest;
	}

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

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(CustomJwtValidationException.class)
	public ResponseEntity<Object> handleCustomJwtValidationException(CustomJwtValidationException ex) {
		// Extract the error message from the exception
		String errorMessage = ex.getError().getDescription();

		// You can return a custom response with the error message
		Map<String, String> errorResponse = new HashMap<>();
		errorResponse.put("error", errorMessage);

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
