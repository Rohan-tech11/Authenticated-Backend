/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Genesis.SwiftSend.Client.ClientServices;
import com.Genesis.SwiftSend.ResponseHandler.ResponseHandler;

import lombok.RequiredArgsConstructor;

/**
 * @author rohan
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
	private final IUserService userService;

	@GetMapping("/getAllClientServices")
	public ResponseEntity<Object> getAllClientServices() {
		HashMap<String, Object> data = new HashMap<>();
		List<ClientServices> allClientServices = userService.getAllClientServices();

		if (allClientServices.isEmpty()) {
			return ResponseHandler.responseBuilder(" No client Services Available, please come back later",
					HttpStatus.OK);
		} else {
			// Map each ClientServices object to a Map with client name included
			List<Map<String, Object>> formattedClientServices = allClientServices.stream().map(clientService -> {
				Map<String, Object> serviceMap = new HashMap<>();
				serviceMap.put("id", clientService.getId());
				serviceMap.put("serviceName", clientService.getServiceName());
				serviceMap.put("serviceDescription", clientService.getServiceDescription());
				serviceMap.put("deliveryTimeDays", clientService.getDeliveryTimeDays());
				serviceMap.put("price", clientService.getPrice());
				serviceMap.put("serviceType", clientService.getServiceType());
				serviceMap.put("clientName", clientService.getClientName());
				return serviceMap;
			}).collect(Collectors.toList());

			data.put("data", formattedClientServices);
			return ResponseHandler.responseBuilder("Fetched all client Services", HttpStatus.OK, data);
		}
	}

	// path variable is suitable for parameters that are required and expected to be
	// present in the url
	@GetMapping("/fetchServiceById/{id}")
	public ResponseEntity<Object> getServiceById(@PathVariable Long id) {

		HashMap<String, Object> clientServiceMap = userService.fetchClientServiceById(id);
		if (!clientServiceMap.isEmpty()) {
			return ResponseHandler.responseBuilder("Fetched the  client service with id  : " + id, HttpStatus.OK,
					clientServiceMap);

		} else {
			return ResponseHandler.responseBuilder(" no  client service with that id  : " + id, HttpStatus.NOT_FOUND);
		}

	}

}
