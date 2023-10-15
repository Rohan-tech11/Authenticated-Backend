/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import java.util.List;
import java.util.Optional;

import com.Genesis.SwiftSend.Registration.RegistrationRequest;
import com.Genesis.SwiftSend.Registration.Token.VerificationToken;

/**
 * @author Rohan
 *
 */
public interface IUserService {
	List<User> getUsers();

	User registerUser(RegistrationRequest request);

	Optional<User> findByEmail(String email);

	void saveUserVerificationToken(User theUser, String verificationToken);

	String validateToken(String theToken);

	VerificationToken generateNewVerificationToken(String oldToken);

}
