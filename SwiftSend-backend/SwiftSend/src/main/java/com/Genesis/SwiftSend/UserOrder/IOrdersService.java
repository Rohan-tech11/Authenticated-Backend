/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.UserOrder;

/**
 * @author rohan
 *
 */
public interface IOrdersService {

	boolean placeOrder(OrderRecord orderRequest, int serviceId, String userEmail);

}
