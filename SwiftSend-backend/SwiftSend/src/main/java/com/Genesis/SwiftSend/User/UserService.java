/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Genesis.SwiftSend.Client.Client;
import com.Genesis.SwiftSend.Client.ClientRepository;
import com.Genesis.SwiftSend.Client.ClientServices;
import com.Genesis.SwiftSend.Client.ClientServicesRepository;
import com.Genesis.SwiftSend.Exception.AccountDisabledException;
import com.Genesis.SwiftSend.Exception.CustomException;
import com.Genesis.SwiftSend.Exception.UserAlreadyExistsException;
import com.Genesis.SwiftSend.Registration.RegistrationRequest;
import com.Genesis.SwiftSend.Registration.Token.JwtTokenService;
import com.Genesis.SwiftSend.Registration.Token.VerificationToken;
import com.Genesis.SwiftSend.Registration.Token.VerificationTokenRepository;
import com.Genesis.SwiftSend.ResponseHandler.LoginResponseDto;
import com.Genesis.SwiftSend.Role.Role;
import com.Genesis.SwiftSend.Role.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rohan
 *
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository tokenRepository;
	private final RoleRepository roleRepository;
	private final AuthenticationManager authenticationManager;
	private final ClientRepository clientRepository;
	private final ClientServicesRepository clientServicesRepository;

	private final JwtTokenService JWTtokenService;

	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public User registerUser(RegistrationRequest request) {

		Optional<User> user = this.findByEmail(request.email());
		Optional<Client> client = clientRepository.findByEmail(request.email());
		Map<String, Object> errorDetails = new HashMap<>();

		if (client.isPresent()) {
			errorDetails.put("field", "email");
			errorDetails.put("code", "EMAIL_EXISTS");
			throw new UserAlreadyExistsException(
					" A  Swift send client with the same email  exists" + request.email() + " already exists",
					HttpStatus.BAD_REQUEST, errorDetails);
		}

		if (user.isPresent()) {
			User loggedInUser = user.get();
			if (loggedInUser.getEmail() != null) {
				errorDetails.put("field", "email");
				errorDetails.put("code", "EMAIL_EXISTS");
				throw new UserAlreadyExistsException("User with email " + request.email() + " already exists",
						HttpStatus.BAD_REQUEST, errorDetails);
			} else if (loggedInUser.getMobileNumber() != null) {
				errorDetails.put("field", "mobile_number");
				errorDetails.put("code", "MOBILE_NUMBER_EXISTS");
				throw new UserAlreadyExistsException("User with number " + request.mobileNumber() + " already exists",
						HttpStatus.BAD_REQUEST, errorDetails);
			}
		}

		var newUser = new User();
		newUser.setFullName(request.fullName());
		newUser.setEmail(request.email());
		newUser.setPassword(passwordEncoder.encode(request.password()));
		newUser.setMobileNumber(request.mobileNumber());
		Role userRole = roleRepository.findByAuthority("USER").get();
		Set<Role> authorities = new HashSet<>();
		authorities.add(userRole);
		newUser.setAuthorities(authorities);
		return userRepository.save(newUser);
	}

	/**
	 * @param mobileNumber
	 * @return
	 */
	public Optional<User> findByMobileNumber(String mobileNumber) {
		// TODO Auto-generated method stub
		return userRepository.findByMobileNumber(mobileNumber);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUserVerificationToken(User theUser, String token) {
		var verificationToken = new VerificationToken(token, theUser);
		tokenRepository.save(verificationToken);
	}

	@Override
	public String validateToken(String theToken) {
		VerificationToken token = tokenRepository.findByToken(theToken);
		if (token == null) {
			return "Invalid verification token";
		}
		User user = token.getUser();
		Calendar calendar = Calendar.getInstance();
		if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
			return "Token already expired";
		}
		user.setEnabled(true);
		userRepository.save(user);
		return "valid";
	}

	/*
	 * If the verificationToken object is retrieved from the database within the
	 * same transaction Hibernates
	 * 
	 * typically tracks changes to managed entities and will update the existing
	 * record in the database with the changes when the transaction is committed.In
	 * this case,you won't see a new row in the database.
	 */

	@Override
	public VerificationToken generateNewVerificationToken(String oldToken) {
		// TODO Auto-generated method stub
		VerificationToken token = tokenRepository.findByToken(oldToken);
		var tokenTime = new VerificationToken();// managed state
		log.info("token expiration time ()" + token.getExpirationTime());
		token.setToken(UUID.randomUUID().toString());
		token.setExpirationTime(token.getTokenExpirationTime());
		return tokenRepository.save(token);
	}

	// authenticate manager will use user details and user details service to
	// authenticate the logged in user
	public LoginResponseDto loginUser(String email, String password) {
		Map<String, Object> errorDetails = new HashMap<>();

		Optional<User> theUser = userRepository.findByEmail(email);

		try {
			Authentication auth;
			// When you call authenticate on it, the ProviderManager will iterate through
			// its list of providers
			// and delegate the authentication to the first provider that supports the given
			// Authentication type.
			if (!theUser.isPresent()) {
				// Perform client authentication
				// user attempts to authenticate by providing a username and password, these
				// credentials are usually encapsulated in a UsernamePasswordAuthenticationToken
				// object.
				auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			} else {
				// Perform user authentication
				auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			}
			log.info(auth + "authentication object");
			String token = JWTtokenService.generateJwt(auth);

			if (!theUser.isPresent()) {
				// Handle client login response
				return new LoginResponseDto(clientRepository.findByEmail(email).get().getEmail(), token);
			} else {
				// Handle user login response
				return new LoginResponseDto(userRepository.findByEmail(email).get().getEmail(), token);
			}

		} catch (AuthenticationException e) {
			// Handle authentication exceptions based on type

			if (e instanceof BadCredentialsException) {
				// Handle invalid username or password
				errorDetails.put("field", "username or password");
				errorDetails.put("code", "INVALID_CREDENTIALS");
				throw new CustomException("Invalid credentials", HttpStatus.BAD_REQUEST, errorDetails);
			} else if (e instanceof LockedException) {
				// Handle locked account
				errorDetails.put("code", "ACCOUNT_LOCKED");
				throw new CustomException("Account is Locked", HttpStatus.BAD_REQUEST, errorDetails);
			} else if (e instanceof AccountDisabledException) {
				// Handle disabled account
				errorDetails.put("code", "ACCOUNT_DISABLED");
				throw new CustomException("Account is disabled", HttpStatus.BAD_REQUEST, errorDetails);
			} else if (e instanceof AccountExpiredException) {
				// Handle expired account
				errorDetails.put("code", "ACCOUNT_EXPIRED");
				throw new CustomException("Account is expired", HttpStatus.BAD_REQUEST, errorDetails);
			} else {
				// Handle other authentication exceptions
				errorDetails.put("code", "EMAIL_VERIFICATIONFAILED");
				throw new CustomException("Email Verification  failed", HttpStatus.BAD_REQUEST, errorDetails);
			}
		}
	}

	@Override
	public List<ClientServices> getAllClientServices() {
		// TODO Auto-generated method stub
		return clientServicesRepository.findAll();
	}

}
