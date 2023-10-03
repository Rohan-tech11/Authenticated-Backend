/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.Exception;

/**
 * @author rohan
 *
 */
public class UserAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistsException(String message) {
		super(message);
	}

}
