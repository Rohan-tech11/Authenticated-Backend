/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author rohan
 *
 */
public class AccountDisabledException extends AuthenticationException {

	public AccountDisabledException(String message) {
		super(message);
	}
}