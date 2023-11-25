/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Admin;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Genesis.SwiftSend.Client.Client;
import com.Genesis.SwiftSend.Client.ClientRepository;
import com.Genesis.SwiftSend.ResponseHandler.ResponseHandler;

import lombok.RequiredArgsConstructor;

/**
 * @author rohan
 *
 */
@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {

	public final ClientRepository clientRepo;

	@Override
	public List<Client> getAllClientAccounts() {
		// TODO Auto-generated method stub
		return clientRepo.findAll();
	}

	@Override
	public List<Client> getAllUnapprovedClientAccounts() {
		List<Client> unapprovedClientAccounts = clientRepo.findByIsAdminApprovedFalseAndIsEnabledTrue();

		if (unapprovedClientAccounts != null && !unapprovedClientAccounts.isEmpty()) {
			return unapprovedClientAccounts;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<Client> getAllapprovedClientAccounts() {
		List<Client> approvedClientAccounts = clientRepo.findByIsAdminApprovedTrueAndIsEnabledTrue();

		if (approvedClientAccounts != null && !approvedClientAccounts.isEmpty()) {
			return approvedClientAccounts;
		} else {
			return Collections.emptyList();
		}
	}

	// In AdminService.java

	@Override
	public ResponseEntity<Object> approveClientAccount(Integer clientId) {
		Optional<Client> optionalClient = clientRepo.findById(clientId);

		if (optionalClient.isPresent()) {
			Client client = optionalClient.get();

			// Check if the client is not already approved
			if (!client.isAdminApproved()) {
				client.setAdminApproved(true);
				clientRepo.save(client);
				return ResponseHandler.responseBuilder("Client account approved successfully", HttpStatus.OK);
			} else {
				return ResponseHandler.responseBuilder("Client account is already approved", HttpStatus.BAD_REQUEST);
			}
		} else {
			return ResponseHandler.responseBuilder("Client not found with ID: " + clientId, HttpStatus.NOT_FOUND);
		}
	}

}
