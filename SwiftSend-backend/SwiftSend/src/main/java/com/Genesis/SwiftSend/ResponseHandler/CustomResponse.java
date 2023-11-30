/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.ResponseHandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * @author rohan
 *
 */
@Data
public class CustomResponse {
	private String message;
	private HttpStatus httpStatus;
	private Object responseData;
	private LocalDateTime timestamp;

	public CustomResponse(String message, HttpStatus httpStatus, Object responseData) {
		this.message = message;
		this.httpStatus = httpStatus;
		this.responseData = responseData;
		this.timestamp = LocalDateTime.now();
	}

	public CustomResponse(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
		this.timestamp = LocalDateTime.now();
	}

}