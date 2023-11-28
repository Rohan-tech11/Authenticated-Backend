/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Genesis.SwiftSend.Client.ClientServices;
import com.Genesis.SwiftSend.ResponseHandler.ResponseHandler;

import lombok.RequiredArgsConstructor;

/**
 * @author rohan
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
	private final IUserService userService;

	@GetMapping("/getAllClientServices")
	public ResponseEntity<Object> getAllClientServices() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		List<ClientServices> allClientAccounts = userService.getAllClientServices();
		if (allClientAccounts.isEmpty()) {
			return ResponseHandler.responseBuilder(" No   client Services Available please come back later",
					HttpStatus.OK);

		} else {
			data.put("data", allClientAccounts);
			return ResponseHandler.responseBuilder(" fetched all  client Services", HttpStatus.OK, data);
		}

	}

	@GetMapping("/test")
	public String test() {
		return "hello";
	}

}
