/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Exception;

/**
 * @author rohan
 *
 */
public class JwtMissingException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JwtMissingException(String message) {
		super(message);
	}
}
