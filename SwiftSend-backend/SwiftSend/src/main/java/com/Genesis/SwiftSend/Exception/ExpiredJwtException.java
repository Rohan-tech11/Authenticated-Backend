/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Exception;

/**
 * @author rohan
 *
 */
public class ExpiredJwtException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpiredJwtException(String message) {
		super(message);
	}

	public ExpiredJwtException(String message, Throwable cause) {
		super(message, cause);
	}
}
