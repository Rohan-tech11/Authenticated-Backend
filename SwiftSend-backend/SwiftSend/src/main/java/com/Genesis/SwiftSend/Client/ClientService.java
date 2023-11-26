/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Genesis.SwiftSend.Exception.UserAlreadyExistsException;
import com.Genesis.SwiftSend.Registration.RegistrationRequestClient;
import com.Genesis.SwiftSend.Registration.Token.JwtTokenService;
import com.Genesis.SwiftSend.Registration.Token.VerificationToken;
import com.Genesis.SwiftSend.Registration.Token.VerificationTokenRepository;
import com.Genesis.SwiftSend.ResponseHandler.LoginResponseDto;
import com.Genesis.SwiftSend.ResponseHandler.ResponseHandler;
import com.Genesis.SwiftSend.Role.Role;
import com.Genesis.SwiftSend.Role.RoleRepository;
import com.Genesis.SwiftSend.User.User;
import com.Genesis.SwiftSend.User.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author rohan
 *
 */
@Service
@RequiredArgsConstructor
public class ClientService implements IclientService {

	private final ClientRepository clientRepo;
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository tokenRepository;
	private final RoleRepository roleRepository;
	private final AuthenticationManager authenticationManager;
	private final ClientServicesRepository clientServiceRepo;

	private final JwtTokenService JWTtokenService;

	@Override
	public List<Client> getClients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Client registerClient(RegistrationRequestClient request) {
		// TODO Auto-generated method stub
		Optional<Client> client = this.findByEmail(request.email());
		Optional<User> theUser = userRepo.findByEmail(request.email());
		Map<String, Object> errorDetails = new HashMap<>();

		if (client.isPresent() || theUser.isPresent()) {
			errorDetails.put("field", "email");
			errorDetails.put("code", "EMAIL_EXISTS");

			String errorMessage = client.isPresent() ? "Client with email " + request.email() + " already exists"
					: "An User with same email " + request.email() + " already registered";

			throw new UserAlreadyExistsException(errorMessage, HttpStatus.BAD_REQUEST, errorDetails);
		}

		if (client.isPresent()) {
			Client loggedInClient = client.get();
			if (loggedInClient.getMobileNumber() != null) {
				errorDetails.put("field", "mobile_number");
				errorDetails.put("code", "MOBILE_NUMBER_EXISTS");
				throw new UserAlreadyExistsException("Client with number " + request.mobileNumber() + " already exists",
						HttpStatus.BAD_REQUEST, errorDetails);
			} else if (loggedInClient.getBusinessNumber() != null || loggedInClient.getRegistryID() != null) {
				errorDetails.put("field", "Business number or Registry Id");
				errorDetails.put("code", "Business number or Registry Id Exists");
				throw new UserAlreadyExistsException("Client with  Business Id or Registry Id already exists",
						HttpStatus.BAD_REQUEST, errorDetails);
			}
		}

		var newClient = new Client();
		newClient.setClientName(request.clientName());
		newClient.setEmail(request.email());
		newClient.setPassword(passwordEncoder.encode(request.password()));
		newClient.setMobileNumber(request.mobileNumber());
		newClient.setAdminApproved(false);
		newClient.setBusinessNumber(request.businessNumber());
		newClient.setRegistryID(request.registryId());
		newClient.setRegisteredOfficeLocation(request.registeredOfficeLocation());

		Role clientRole = roleRepository.findByAuthority("CLIENT").get();
		Set<Role> authorities = new HashSet<>();
		authorities.add(clientRole);
		newClient.setAuthorities(authorities);
		return clientRepo.save(newClient);
	}

	@Override
	public Optional<Client> findByEmail(String email) {
		// TODO Auto-generated method stub
		return clientRepo.findByEmail(email);
	}

	@Override
	public Optional<Client> findByMobileNumber(String mobileNumber) {
		// TODO Auto-generated method stub
		return clientRepo.findByMobileNumber(mobileNumber);
	}

	@Override
	public void saveClientVerificationToken(Client theClient, String verificationToken) {
		var verificationTokenClient = new VerificationToken(verificationToken, theClient);
		tokenRepository.save(verificationTokenClient);

	}

	/*
	 * If the verificationToken object is retrieved from the database within the
	 * same transaction Hibernates
	 * 
	 * typically tracks changes to managed entities and will update the existing
	 * record in the database with the changes when the transaction is committed.In
	 * this case,you won't see a new row in the database. fetching the client using
	 * the validation token
	 */

	@Override
	public String validateToken(String theToken) {
		VerificationToken token = tokenRepository.findByToken(theToken);
		if (token == null) {
			return "Invalid verification token";
		}
		Client client = token.getClient();
		Calendar calendar = Calendar.getInstance();
		if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
			return "Token already expired";
		}
		client.setEnabled(true);
		clientRepo.save(client);
		return "valid";
	}

	@Override
	public VerificationToken generateNewVerificationToken(String oldToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoginResponseDto loginClient(String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Client> findByBusinessNumber(String businessNumber) {
		// TODO Auto-generated method stub
		return clientRepo.findByBusinessNumber(businessNumber);
	}

	@Override
	public Optional<Client> findByRegistryID(String registryID) {
		// TODO Auto-generated method stub
		return clientRepo.findByRegistryID(registryID);
	}

	@Override
	public ResponseEntity<Object> addService(ClientServiceRequest serviceRequest, String email) {
		// Check if the client is approved by admin
		Optional<Client> loggedInClient = clientRepo.findByEmail(email);

		if (loggedInClient.isPresent() && loggedInClient.get().isAdminApproved()) {
			// Client is approved, proceed to add the service
			Client client = loggedInClient.get();

			// Create and save the service entity
			ClientServices service = new ClientServices();
			service.setClient(client);
			service.setServiceName(serviceRequest.serviceName());
			service.setServiceDescription(serviceRequest.serviceDescription());
			service.setDeliveryTimeDays(serviceRequest.deliveryTimeDays());
			service.setPrice(serviceRequest.price());

			ClientServices NewlyAddedservice = clientServiceRepo.save(service);
			return ResponseHandler.responseBuilder("Service added Successfully", HttpStatus.CREATED, NewlyAddedservice);
		} else {
			// Client is not approved, throw an exception
			return ResponseHandler.responseBuilder("Client is not approved by admin. Cannot add service.",
					HttpStatus.FORBIDDEN);
		}

	}
}
