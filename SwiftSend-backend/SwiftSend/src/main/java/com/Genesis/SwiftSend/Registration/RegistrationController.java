/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.Registration;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Genesis.SwiftSend.Event.RegistrationCompleteEvent;
import com.Genesis.SwiftSend.Event.Listener.RegistrationCompleteEventListener;
import com.Genesis.SwiftSend.Registration.Token.VerificationToken;
import com.Genesis.SwiftSend.Registration.Token.VerificationTokenRepository;
import com.Genesis.SwiftSend.ResponseHandler.LoginResponseDto;
import com.Genesis.SwiftSend.ResponseHandler.ResponseHandler;
import com.Genesis.SwiftSend.User.User;
import com.Genesis.SwiftSend.User.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rohan
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
@Slf4j
@CrossOrigin("*")
public class RegistrationController {

	private final UserService userService;
	private final ApplicationEventPublisher publisher;
	private final VerificationTokenRepository tokenRepository;
	private final HttpServletRequest servletRequest;
	private final RegistrationCompleteEventListener eventListener;

	@PostMapping
	public ResponseEntity<Object> registerUser(@RequestBody RegistrationRequest registrationRequest,
			final HttpServletRequest request) {
		User user = userService.registerUser(registrationRequest);
		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
		return ResponseHandler.responseBuilder("Success!  Please, check your email for to complete your registration",
				HttpStatus.CREATED, user);
	}

	@GetMapping("/verifyEmail")
	public ResponseEntity<Object> verifyEmail(@RequestParam("token") String token) {
		String requestUrl = new String(
				applicationUrl(servletRequest) + "/register/resend-verification-token?token=" + token.toString());
		VerificationToken theToken = tokenRepository.findByToken(token);
		if (theToken.getUser().isEnabled()) {

			return ResponseHandler.responseBuilder("This account has already been verified, please, login.",
					HttpStatus.OK);
		}
		String verificationResult = userService.validateToken(token);

		// a Map to hold the data you want to send in the response
		Map<String, Object> data = new HashMap<>();
		data.put("requestUrl", requestUrl);
		if (verificationResult.equalsIgnoreCase("valid")) {
			return ResponseHandler.responseBuilder("Email verified successfully, now you can login to your account",
					HttpStatus.OK);
		}
		/*
		 * "Invalid verification token , <a href=\"" + url +
		 * "\"> Get a new Verification Link . </a>";
		 */ return ResponseHandler.responseBuilder("Token got expired please request for a new verification link",
				HttpStatus.NOT_FOUND, data);
	}

	@GetMapping("/resend-verification-token")
	public ResponseEntity<Object> resendVerificationToken(@RequestParam("token") String oldToken,
			HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
		VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken.toString());
		log.info("token object " + verificationToken);
		User theUser = verificationToken.getUser();
		resendVerificationTokenEmail(theUser, applicationUrl(request), verificationToken);
		return ResponseHandler.responseBuilder("A new verification token has been sent", HttpStatus.CREATED);
	}

	private void resendVerificationTokenEmail(User theUser, String applicationUrl, VerificationToken token)
			throws MessagingException, UnsupportedEncodingException {
		String url = applicationUrl + "/register/verifyEmail?token=" + token.getToken();
		eventListener.sendVerificationEmail(url);
		log.info("Click the link to verify your registration :  {}", url);
	}

	public String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	@PostMapping("/login")
	public LoginResponseDto loginUser(@RequestBody LoginRequest body) {
		return userService.loginUser(body.email(), body.password());
	}

}
