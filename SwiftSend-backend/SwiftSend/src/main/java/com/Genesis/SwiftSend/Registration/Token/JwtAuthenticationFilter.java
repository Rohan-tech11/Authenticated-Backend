/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Registration.Token;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();

		// Check if the request path is not related to registration or login
		if (!requestURI.startsWith("/register") && !requestURI.startsWith("/login")
				&& !requestURI.startsWith("/swagger-ui") && !requestURI.startsWith("/v3/api-docs")) {
			// Verify the JWT as before
			String jwt = extractJwtFromRequest(request);
			String authorizationHeader = request.getHeader("Authorization");
			log.info("authorized header is" + authorizationHeader);
			log.info("generated jwt token is" + jwt);
			if (jwt == null || jwt.isEmpty()) {
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				// Create and write an error JSON response
				String errorResponse = "jwt is missing in the request";
				HttpStatus code = HttpStatus.UNAUTHORIZED;
				String jsonResponse = "{\"error\": \"" + errorResponse + "\", \"HttpStatusCode\": " + code.value()
						+ "}";
				response.getWriter().write(jsonResponse);
			} else {
				// Continue with request processing
				filterChain.doFilter(request, response);
			}
		} else {
			// Request path is related to registration or login, skip JWT verification
			filterChain.doFilter(request, response);
		}
	}

	private String extractJwtFromRequest(HttpServletRequest request) {
		// Typically, JWTs are included in the "Authorization" header, like "Bearer
		// <token>"
		String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			// Remove "Bearer " to get the actual JWT token
			return authorizationHeader.substring(7);
		}

		// If the JWT is not found in the "Authorization" header, you may check other
		// headers or request parameters
		// For example, if you use a custom header like "X-JWT-Token", you can check it
		// here

		// If not found in any header, you may check request parameters or cookies, if
		// applicable

		return null; // JWT not found in the request
	}

}
