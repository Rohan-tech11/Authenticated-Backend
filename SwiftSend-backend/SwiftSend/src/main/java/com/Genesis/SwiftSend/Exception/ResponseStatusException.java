/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

/**
 * @author rohan
 *
 */
public class ResponseStatusException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final HttpStatus httpStatus;
	private Map<String, Object> errorDetails;

	public ResponseStatusException(String message, HttpStatus httpStatus, Map<String, Object> errorDetails) {
		super(message);
		this.httpStatus = httpStatus;
		this.errorDetails = errorDetails;
	}

	public ResponseStatusException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public Map<String, Object> getErrorDetails() {
		return errorDetails;
	}
}
