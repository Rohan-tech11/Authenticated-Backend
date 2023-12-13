/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.UserOrder;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.Genesis.SwiftSend.Client.Client;

/**
 * @author rohan
 *
 */
@Service
public interface IOrdersService {

	boolean placeOrder(OrderRecord orderRequest, int serviceId, String userEmail);

	List<Map<String, Object>> getOrdersByClient(Client client);

}
