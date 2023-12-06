/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Admin;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Genesis.SwiftSend.Client.Client;
import com.Genesis.SwiftSend.ResponseHandler.ResponseHandler;

import lombok.RequiredArgsConstructor;

/**
 * @author rohan
 *
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AdminController {

	private final IAdminService adminService;

	@GetMapping("/test")
	public String test() {
		return "hello";
	}

	@GetMapping("/getAllClientAccounts")
	public ResponseEntity<Object> getAllClientAccounts() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		List<Client> allClientAccounts = adminService.getAllClientAccounts();
		if (allClientAccounts != null) {
			data.put("data", allClientAccounts);
			return ResponseHandler.responseBuilder(" fetched all approved and unaproved client accounts", HttpStatus.OK,
					data);
		} else {
			return ResponseHandler.responseBuilder(" No approved and unaproved client accounts", HttpStatus.OK);
		}

	}

	@GetMapping("/getAllRegisteredClientAccounts")
	public ResponseEntity<Object> getAllRegisterdClientAccounts() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		List<Client> allClientAccounts = adminService.getAllRegisteredClientAccounts();
		if (allClientAccounts != null) {
			data.put("data", allClientAccounts);
			return ResponseHandler.responseBuilder(" fetched all registered client accounts", HttpStatus.OK, data);
		} else {
			return ResponseHandler.responseBuilder(" No registered client accounts", HttpStatus.OK);
		}

	}

	@GetMapping("/getAllUnapprovedClientAccounts")
	public ResponseEntity<Object> getAllUnapprovedClientAccounts() {
		HashMap<String, Object> data = new HashMap<>();
		List<Client> unapprovedClientAccounts = adminService.getAllUnapprovedClientAccounts();

		if (unapprovedClientAccounts != null && !unapprovedClientAccounts.isEmpty()) {
			data.put("data", unapprovedClientAccounts);
			return ResponseHandler.responseBuilder("Fetched all unapproved client accounts", HttpStatus.OK, data);
		} else {
			return ResponseHandler.responseBuilder("No unapproved client accounts found", HttpStatus.OK);
		}
	}

	@GetMapping("/getAllapprovedClientAccounts")
	public ResponseEntity<Object> getAllapprovedClientAccounts() {
		HashMap<String, Object> data = new HashMap<>();
		List<Client> unapprovedClientAccounts = adminService.getAllapprovedClientAccounts();

		if (unapprovedClientAccounts != null && !unapprovedClientAccounts.isEmpty()) {
			data.put("data", unapprovedClientAccounts);
			return ResponseHandler.responseBuilder("Fetched all approved client accounts", HttpStatus.OK, data);
		} else {
			return ResponseHandler.responseBuilder("No approved client accounts found", HttpStatus.OK);
		}
	}

	@GetMapping("/getAllUnVerifiedClientAccounts")
	public ResponseEntity<Object> getAllaUnVerifiedClientAccounts() {
		HashMap<String, Object> data = new HashMap<>();
		List<Client> unapprovedClientAccounts = adminService.getAllUnVerifiedClientAccounts();

		if (unapprovedClientAccounts != null && !unapprovedClientAccounts.isEmpty()) {
			data.put("data", unapprovedClientAccounts);
			return ResponseHandler.responseBuilder("Fetched all un verified client accounts", HttpStatus.OK, data);
		} else {
			return ResponseHandler.responseBuilder("No unverified client accounts found", HttpStatus.OK);
		}
	}

	@PutMapping("/approveClientAccount")
	public ResponseEntity<Object> approveClientAccount(@RequestParam(value = "clientId") Integer clientId) {

		return adminService.approveClientAccount(clientId);

	}

}
