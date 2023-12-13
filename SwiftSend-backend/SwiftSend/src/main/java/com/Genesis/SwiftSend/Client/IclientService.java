/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import java.util.List;
import java.util.Map;
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

	void saveClientVerificationToken(Client theClient, String verificationToken);

	String validateToken(String theToken);

	VerificationToken generateNewVerificationToken(String oldToken);

	LoginResponseDto loginClient(String email, String password);

	/**
	 * @param serviceRequest
	 * @return
	 */
	ResponseEntity<Object> addService(ClientServiceRequest serviceRequest, String email);

	boolean isAdminApproved(String clientEmail);

	List<Map<String, Object>> fetchClientServices(String email);

	/**
	 * @param serviceId
	 * @return
	 */
	ClientServices findByClientService(long serviceId);

	void createQuotation(Long orderId, QuotationRequest quotationRequest);

}
