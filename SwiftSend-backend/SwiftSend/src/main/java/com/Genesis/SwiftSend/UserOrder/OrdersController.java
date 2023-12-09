/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.UserOrder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Genesis.SwiftSend.ResponseHandler.ResponseHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rohan
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/orders")
@CrossOrigin("*")
@Slf4j
public class OrdersController {

	private final IOrdersService orderService;

	@PostMapping("/placeorder/{id}")
	public ResponseEntity<Object> placeOrder(@PathVariable("id") int id, @RequestBody OrderRecord orderRequest,
			Authentication authentication) {

		String userEmail = ((Jwt) authentication.getPrincipal()).getClaim("email");

		orderService.placeOrder(orderRequest, id, userEmail);

		return ResponseHandler.responseBuilder("Order Placed Successfully", HttpStatus.CREATED);
	}

}
