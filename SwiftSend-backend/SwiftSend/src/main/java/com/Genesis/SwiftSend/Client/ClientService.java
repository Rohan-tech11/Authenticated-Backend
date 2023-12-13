/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Genesis.SwiftSend.Event.SendQuotationEvent;
import com.Genesis.SwiftSend.Exception.ResponseStatusException;
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
import com.Genesis.SwiftSend.UserOrder.Orders;
import com.Genesis.SwiftSend.UserOrder.OrdersRepository;

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
	private final ClientQuotationRepository clientQuotationRepo;
	private final OrdersRepository ordersRepo;
	private final ApplicationEventPublisher publisher;

	private final JwtTokenService JWTtokenService;

	@Override
	public List<Client> getClients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Client registerClient(RegistrationRequestClient request) {
		Optional<Client> existingClient = this.findByEmail(request.email());
		Optional<User> existingUser = userRepo.findByEmail(request.email());
		Map<String, Object> errorDetails = new HashMap<>();

		if (existingClient.isPresent() || existingUser.isPresent()) {
			errorDetails.put("field", "email");
			errorDetails.put("code", "EMAIL_EXISTS");

			String errorMessage = existingClient.isPresent()
					? "Client with email " + request.email() + " already exists"
					: "An User with same email " + request.email() + " already registered";

			throw new UserAlreadyExistsException(errorMessage, HttpStatus.BAD_REQUEST, errorDetails);
		}

		try {
			// Check for other unique constraints
			if (request.mobileNumber() != null && clientRepo.existsByMobileNumber(request.mobileNumber())) {
				errorDetails.put("field", "mobile_number");
				errorDetails.put("code", "MOBILE_NUMBER_EXISTS");
				throw new UserAlreadyExistsException("Client with number " + request.mobileNumber() + " already exists",
						HttpStatus.BAD_REQUEST, errorDetails);
			}

			if (request.businessRegistryId() != null
					&& clientRepo.existsByBusinessRegistryId(request.businessRegistryId())) {
				errorDetails.put("field", "Business Registry Id");
				errorDetails.put("code", "BUSINESS_REGISTRY_ID_EXISTS");
				throw new UserAlreadyExistsException(
						"Client with Business Registry Id " + request.businessRegistryId() + " already exists",
						HttpStatus.BAD_REQUEST, errorDetails);
			}

			// No unique constraint violation, proceed with client registration
			var newClient = new Client();
			newClient.setBusinessName(request.businessName());
			newClient.setEmail(request.email());
			newClient.setPassword(passwordEncoder.encode(request.password()));
			newClient.setMobileNumber(request.mobileNumber());
			newClient.setAdminApproved(false);
			newClient.setBusinessRegistryId(request.businessRegistryId());
			newClient.setRegisteredOfficeLocation(request.registeredOfficeLocation());

			Role clientRole = roleRepository.findByAuthority("CLIENT").get();
			Set<Role> authorities = new HashSet<>();
			authorities.add(clientRole);
			newClient.setAuthorities(authorities);

			return clientRepo.save(newClient);
		} catch (DataIntegrityViolationException e) {
			// Handle other database-related exceptions here
			errorDetails.put("code", "DATABASE_ERROR");
			throw new UserAlreadyExistsException("Error while processing client registration",
					HttpStatus.INTERNAL_SERVER_ERROR, errorDetails);
		}
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
		return clientRepo.findByBusinessRegistryId(businessNumber);
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
			service.setServiceType(serviceRequest.serviceType());

			ClientServices NewlyAddedservice = clientServiceRepo.save(service);
			return ResponseHandler.responseBuilder("Service added Successfully", HttpStatus.CREATED, NewlyAddedservice);
		} else {
			// Client is not approved, throw an exception
			return ResponseHandler.responseBuilder("Client is not approved by admin. Cannot add service.",
					HttpStatus.FORBIDDEN);
		}

	}

	@Override
	public boolean isAdminApproved(String clientEmail) {
		// TODO Auto-generated method stub
		Optional<Client> client = clientRepo.findByEmail(clientEmail);
		boolean adminApproved = false;
		if (client.isPresent()) {
			adminApproved = client.get().isAdminApproved();
			return adminApproved;
		} else {
			return adminApproved;
		}
	}

	@Override
	public List<Map<String, Object>> fetchClientServices(String email) {
		List<ClientServices> clientServices = clientServiceRepo.findByClientEmail(email);

		// The entire structure is a list (List<Map<String, Object>>) where each element
		// is a map.
		if (!clientServices.isEmpty()) {
			// Map each ClientServices object to a Map with client name included
			return clientServices.stream().map(clientService -> {
				Map<String, Object> serviceMap = new HashMap<>();
				serviceMap.put("id", clientService.getId());
				serviceMap.put("serviceName", clientService.getServiceName());
				serviceMap.put("serviceDescription", clientService.getServiceDescription());
				serviceMap.put("deliveryTimeDays", clientService.getDeliveryTimeDays());
				serviceMap.put("price", clientService.getPrice());
				serviceMap.put("serviceType", clientService.getServiceType());
				serviceMap.put("businessName", clientService.getClientName());
				return serviceMap;
			}).collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public ClientServices findByClientService(long serviceId) {
		// Find the ClientServices by serviceId
		ClientServices clientService = clientServiceRepo.findById(serviceId)
				.orElseThrow(() -> new ResponseStatusException("Client Services not found with id: " + serviceId,
						HttpStatus.NOT_FOUND));

		// Get the associated Client from ClientServices

		return clientService;
	}

	public static double calculateTax(double price) {
		// Assuming tax is 13% of the total price
		double taxRate = 0.13;
		return price * taxRate;
	}

	public static double calculatePlatformFee(double price) {
		// Assuming platform fee is 10% of the total price
		double platformFeeRate = 0.10;
		return price * platformFeeRate;
	}

	@Override
	public void createQuotation(Long orderId, QuotationRequest quotationRequest) {
		double price = quotationRequest.price();
		double platformFee = calculatePlatformFee(price);
		double tax = calculateTax(price);
		Orders order = ordersRepo.findById(orderId).orElseThrow(() -> {
			throw new ResponseStatusException("No orders for this ID : " + orderId, HttpStatus.NOT_FOUND);
		});

		User theUser = order.getUser();
		Client client = order.getClient();
		ClientQuotation quotation = new ClientQuotation();

		quotation.setClient(client);
		quotation.setOrder(order);
		quotation.setDeliveryDays(quotationRequest.deliveryDays());
		quotation.setPrice(price);
		quotation.setPlatformFee(calculatePlatformFee(price));
		quotation.setTax(calculateTax(price));
		quotation.setTotalCost(price + platformFee + tax);

		clientQuotationRepo.save(quotation);

		publisher.publishEvent(new SendQuotationEvent(theUser, quotation));
	}

}
