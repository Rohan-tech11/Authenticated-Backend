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

/**
 * @author rohan
 *
 */
@Service
public class JwtTokenService {

	@Autowired
	private JwtEncoder jwtEncoder;

	@Autowired
	private JwtDecoder jwtDecoder;

	public String generateJwt(Authentication auth) {

		Instant currentTime = Instant.now();
		Instant expirationTime = currentTime.plusSeconds(1800); // 30 minutes in seconds

		String scope = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("SwiftSendService")
				.issuedAt(currentTime)
				.subject(auth.getName())
				.expiresAt(expirationTime) // Set the expiration time
				.claim("roles", scope)
				.build();

		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

}
