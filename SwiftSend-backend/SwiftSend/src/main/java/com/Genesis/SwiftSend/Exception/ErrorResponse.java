/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Exception;

/**
 * @author rohan
 *
 */
public class ErrorResponse {
	private Error error;
	private String errorDescription;

	public enum Error {
		INVALID_REQUEST, // You can add more error types as needed
		// Add additional error types here
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}
