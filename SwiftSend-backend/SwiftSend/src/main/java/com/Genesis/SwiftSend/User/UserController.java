/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import java.util.List;

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
@RequestMapping("/users")
public class UserController {
	private final IUserService userService;

	@GetMapping
	public List<User> getUsers() {
		return userService.getUsers();
	}

}
