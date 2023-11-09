///**
// * created by @Rohan
// */
//package com.Genesis.SwiftSend.Registration.Token;
//
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//
//public class CustomJwtDecoder implements JwtDecoder {
//
//	private final JwtDecoder jwtDecoder;
//
//	public CustomJwtDecoder(JwtDecoder jwtDecoder) {
//		this.jwtDecoder = jwtDecoder;
//	}
//
////	@Override
////	public Jwt decode(String token) throws CustomJwtValidationException {
////		Jwt jwt = jwtDecoder.decode(token);
////
////		// Perform custom token expiration validation
////		if (jwt.getExpiresAt() != null && jwt.getExpiresAt().isBefore(Instant.now())) {
////			OAuth2Error error = new OAuth2Error("token_expired", "JWT token has expired", null);
////			throw new CustomJwtValidationException(error);
////		}
////
////		return jwt;
////	}
//
//}
