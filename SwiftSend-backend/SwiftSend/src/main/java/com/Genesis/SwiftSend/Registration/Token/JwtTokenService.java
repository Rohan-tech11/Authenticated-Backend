/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Registration.Token;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author rohan
 *
 */
@Service
@Slf4j
public class JwtTokenService {

	@Autowired
	private JwtEncoder jwtEncoder;

	@Autowired
	private JwtDecoder jwtDecoder;

//	todo:add the expiration time and add the  jwtfilter to throw the exceptions
	public String generateJwt(Authentication auth) {

		Instant currentTime = Instant.now();
//		Instant expirationTime = currentTime.plusSeconds(120); // 30 minutes in seconds

		String scope = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));

		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("SwiftSendService").issuedAt(currentTime)
				.subject(auth.getName()) // Set the expiration time
				.claim("roles", scope).build();

		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

	/**
	 * @param jwtToken
	 * @return
	 */

//	public String extractUsername(String jwtToken) {
//		try {
//			// Decode and validate the JWT token using the JwtDecoder
//			Jwt jwt = jwtDecoder.decode(jwtToken);
//
//			// Extract the subject (username) from the JWT claims
//			return jwt.getSubject();
//		} catch (JwtValidationException ex) {
//			// Handle JWT validation errors
//			log.error("JWT validation failed: " + ex.getMessage());
//			// You can choose how to handle validation errors, such as returning null or
//			// throwing an exception
//			return null; // Return null if token is not valid
//		}
//	}
//
//	public boolean isTokenValid(String jwtToken, User userDetails) {
//		try {
//			// Decode and validate the JWT token using the JwtDecoder
//			Jwt jwt = jwtDecoder.decode(jwtToken);
//
//			// Extract the subject (typically the username) from the JWT
//			String usernameFromToken = jwt.getClaim("sub");
//
//			// Compare the username from the token with the UserDetails username
//			return usernameFromToken.equals(userDetails.getFullName());
//		} catch (JwtValidationException ex) {
//			// Handle JWT validation errors
//			log.error("JWT validation failed: " + ex.getMessage());
//			return false;
//		}
//	}
}
