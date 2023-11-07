/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author rohan
 *
 */
public class CustomJwtValidationException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OAuth2Error error;

	public CustomJwtValidationException(OAuth2Error error) {
		super(error.getDescription());
		this.error = error;
	}

	public OAuth2Error getError() {
		return error;
	}
}
