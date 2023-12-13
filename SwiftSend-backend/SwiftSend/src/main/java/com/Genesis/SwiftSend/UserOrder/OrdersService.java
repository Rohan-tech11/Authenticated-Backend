/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.UserOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Genesis.SwiftSend.Client.Client;
import com.Genesis.SwiftSend.Client.ClientServices;
import com.Genesis.SwiftSend.Client.ClientServicesRepository;
import com.Genesis.SwiftSend.Exception.ResponseStatusException;
import com.Genesis.SwiftSend.User.User;
import com.Genesis.SwiftSend.User.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rohan
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService implements IOrdersService {

	private final OrdersRepository ordersRepo;
	private final UserRepository userRepo;
	private final ClientServicesRepository clientServiceRepo;

	@Override
	public boolean placeOrder(OrderRecord orderRequest, int serviceId, String userEmail) {
		try {
			// Find ClientServices by serviceId
			ClientServices clientServices = getClientServices(serviceId);

			// Find the logged-in user by email
			User loggedInUser = userRepo.findByEmail(userEmail).orElseThrow(
					() -> new ResponseStatusException("User not found with email: " + userEmail, HttpStatus.NOT_FOUND));

			// Create a new order
			Orders newOrder = new Orders();
			newOrder.setClient(clientServices.getClient());
			newOrder.setClientServices(clientServices);
			newOrder.setDestination(orderRequest.destination());
			newOrder.setDimensions(orderRequest.dimensions());
			newOrder.setImageURL(orderRequest.imageUrl());
			newOrder.setPremium(orderRequest.premium());
			newOrder.setSource(orderRequest.source());
			newOrder.setType(orderRequest.type());
			newOrder.setUser(loggedInUser);
			newOrder.setWeight(orderRequest.weight());

			// Save the new order
			ordersRepo.save(newOrder);

			// Return true to indicate success
			return true;

		} catch (Exception e) {
			log.info(" exceptions is " + e.getMessage());
			if (e instanceof ResponseStatusException) {
				throw new ResponseStatusException("Client Services not found with id: " + serviceId,
						HttpStatus.NOT_FOUND);
			} else {
				throw new ResponseStatusException("Order placement failed", HttpStatus.INTERNAL_SERVER_ERROR);

			}
			// Handle other exceptions and return false
		}
	}

	private ClientServices getClientServices(int serviceId) {
		return clientServiceRepo.findById((long) serviceId)
				.orElseThrow(() -> new ResponseStatusException("Client Services not found with id: " + serviceId,
						HttpStatus.NOT_FOUND));
	}

	@Override
	public List<Map<String, Object>> getOrdersByClient(Client client) {
		List<Orders> orders = ordersRepo.findByClient(client);

		if (!orders.isEmpty()) {

			return orders.stream().map(order -> {

				User customer = order.getUser();

				Map<String, Object> orderMap = new HashMap<>();
				orderMap.put("OrderId", order.getId());
				orderMap.put("CustomerName", customer.getFullName());
				orderMap.put("CustomerEmailAddress", customer.getEmail());
				orderMap.put("CustomerContact", customer.getMobileNumber());
				orderMap.put("ServiceName", order.getClientServices().getServiceName());
				orderMap.put("Source", order.getSource());
				orderMap.put("Destination", order.getDestination());
				orderMap.put("Premium", order.getPremium());
				orderMap.put("Dimensions", order.getDimensions());
				orderMap.put("Weight", order.getWeight());
				orderMap.put("Type", order.getType());
				orderMap.put("ImaegUrl", order.getImageURL());
				orderMap.put("PricePerKm", order.getClientServices().getPrice());

				return orderMap;
			}).collect(Collectors.toList());

		}

		throw new ResponseStatusException("No orders found for client: " + client.getBusinessName(),
				HttpStatus.NOT_FOUND);
	}

}
