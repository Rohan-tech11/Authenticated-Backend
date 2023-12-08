/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.Genesis.SwiftSend.Client.ClientServices;
import com.Genesis.SwiftSend.Registration.RegistrationRequest;
import com.Genesis.SwiftSend.Registration.Token.VerificationToken;
import com.Genesis.SwiftSend.ResponseHandler.LoginResponseDto;

/**
 * @author Rohan
 *
 */
public interface IUserService {
	List<User> getUsers();

	User registerUser(RegistrationRequest request);

	Optional<User> findByEmail(String email);

	Optional<User> findByMobileNumber(String mobileNumber);

	void saveUserVerificationToken(User theUser, String verificationToken);

	String validateToken(String theToken);

	VerificationToken generateNewVerificationToken(String oldToken);

	LoginResponseDto loginUser(String email, String password);

	List<ClientServices> getAllClientServices();

	HashMap<String, Object> fetchClientServiceById(Long id);

}
