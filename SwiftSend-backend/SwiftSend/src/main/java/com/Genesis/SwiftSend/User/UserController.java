/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping
	public List<User> getUsers() {
		return userService.getUsers();
	}

	@GetMapping("/test")
	public String test() {
		return "hello";
	}

}
