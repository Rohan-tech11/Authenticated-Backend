/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.ResponseHandler;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rohan
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

	private String userName;
	private String jwt;
	private Collection<? extends GrantedAuthority> authorities;

}
