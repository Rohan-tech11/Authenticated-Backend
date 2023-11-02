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
public class CustomException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final HttpStatus httpStatus;
	private final Map<String, Object> errorDetails;

	public CustomException(String message, HttpStatus httpStatus, Map<String, Object> errorDetails) {
		super(message);
		this.httpStatus = httpStatus;
		this.errorDetails = errorDetails;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public Map<String, Object> getErrorDetails() {
		return errorDetails;
	}
}
