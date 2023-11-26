/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * @author rohan
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class ClientController {

	private final ClientService clientService;

	@PostMapping("/addService")
	public ResponseEntity<Object> addService(@RequestBody ClientServiceRequest serviceRequest,
			Authentication authentication) {

		String userEmail = authentication.getName(); // Get the email of the authenticated user
		return clientService.addService(serviceRequest, userEmail);

	}

}
