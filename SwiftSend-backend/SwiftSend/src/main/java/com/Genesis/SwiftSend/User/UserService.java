/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Genesis.SwiftSend.Exception.UserAlreadyExistsException;
import com.Genesis.SwiftSend.Registration.RegistrationRequest;
import com.Genesis.SwiftSend.Registration.Token.JwtTokenService;
import com.Genesis.SwiftSend.Registration.Token.VerificationToken;
import com.Genesis.SwiftSend.Registration.Token.VerificationTokenRepository;
import com.Genesis.SwiftSend.ResponseHandler.LoginResponseDto;
import com.Genesis.SwiftSend.Role.Role;
import com.Genesis.SwiftSend.Role.RoleRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author Rohan
 *
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository tokenRepository;
	private final RoleRepository roleRepository;
	private final AuthenticationManager authenticationManager;

	private final JwtTokenService JWTtokenService;

	@Override
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public User registerUser(RegistrationRequest request) {
		Optional<User> user = this.findByEmail(request.email());
		Optional<User> userByMobile = this.findByMobileNumber(request.mobileNumber());

		if (user.isPresent()) {
			throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
		}
		if (userByMobile.isPresent()) {
			throw new UserAlreadyExistsException(
					"User with mobile number " + request.mobileNumber() + " is already registered");
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
		System.out.println("token time is " + token.getExpirationTime());
		token.setToken(UUID.randomUUID().toString());
		token.setExpirationTime(token.getTokenExpirationTime());
		return tokenRepository.save(token);
	}

	// authenticate manager will use user details and user details service to
	// authenticate the logged in user
	public LoginResponseDto loginUser(String email, String password) {

		try {
			Authentication auth = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(email, password));

			String token = JWTtokenService.generateJwt(auth);

			return new LoginResponseDto(userRepository.findByEmail(email).get().getEmail(), token);

		} catch (AuthenticationException e) {
			return new LoginResponseDto(null, "");
		}
	}

}
