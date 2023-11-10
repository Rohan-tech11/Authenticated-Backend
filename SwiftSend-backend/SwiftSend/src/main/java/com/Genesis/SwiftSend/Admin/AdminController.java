/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Admin;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rohan
 *
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

	@GetMapping("/test")
	public String test() {
		return "hello";
	}

}
