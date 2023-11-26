/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.Genesis.SwiftSend.Registration.RegistrationRequestClient;
import com.Genesis.SwiftSend.Registration.Token.VerificationToken;
import com.Genesis.SwiftSend.ResponseHandler.LoginResponseDto;

/**
 * @author rohan
 *
 */
public interface IclientService {

	List<Client> getClients();

	Client registerClient(RegistrationRequestClient request);

	Optional<Client> findByEmail(String email);

	Optional<Client> findByMobileNumber(String mobileNumber);

	Optional<Client> findByBusinessNumber(String businessNumber);

	Optional<Client> findByRegistryID(String registryID);

	void saveClientVerificationToken(Client theClient, String verificationToken);

	String validateToken(String theToken);

	VerificationToken generateNewVerificationToken(String oldToken);

	LoginResponseDto loginClient(String email, String password);

	/**
	 * @param serviceRequest
	 * @return
	 */
	ResponseEntity<Object> addService(ClientServiceRequest serviceRequest, String email);

}
