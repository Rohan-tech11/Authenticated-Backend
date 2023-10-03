/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.Registration;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Genesis.SwiftSend.Event.RegistrationCompleteEvent;
import com.Genesis.SwiftSend.Registration.Token.VerificationToken;
import com.Genesis.SwiftSend.Registration.Token.VerificationTokenRepository;
import com.Genesis.SwiftSend.User.User;
import com.Genesis.SwiftSend.User.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * @author rohan
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

	private final UserService userService;
	private final ApplicationEventPublisher publisher;
	private final VerificationTokenRepository tokenRepository;

	@PostMapping
	public String registerUser(
			@RequestBody RegistrationRequest registrationRequest,
			final HttpServletRequest request) {
		User user = userService.registerUser(registrationRequest);
		publisher.publishEvent(
				new RegistrationCompleteEvent(user, applicationUrl(request)));
		return "Success!  Please, check your email for to complete your registration";
	}

	@GetMapping("/verifyEmail")
	public String verifyEmail(@RequestParam("token") String token) {
		VerificationToken theToken = tokenRepository.findByToken(token);
		if (theToken.getUser().isEnabled()) {
			return "This account has already been verified, please, login.";
		}
		String verificationResult = userService.validateToken(token);
		if (verificationResult.equalsIgnoreCase("valid")) {
			return "Email verified successfully. Now you can login to your account";
		}
		return "Invalid verification token";
	}

	public String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath();
	}

}