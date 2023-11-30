package com.Genesis.SwiftSend.ResponseHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author rohan
 *
 */

public class ResponseHandler {

	public static ResponseEntity<Object> responseBuilder(String message, HttpStatus httpStatus, Object responseObject) {
		CustomResponse customResponse = new CustomResponse(message, httpStatus, responseObject);
		return new ResponseEntity<>(customResponse, httpStatus);
	}

	public static ResponseEntity<Object> responseBuilder(String message, HttpStatus httpStatus) {
		CustomResponse customResponse = new CustomResponse(message, httpStatus);

		return new ResponseEntity<>(customResponse, httpStatus);
	}
}
