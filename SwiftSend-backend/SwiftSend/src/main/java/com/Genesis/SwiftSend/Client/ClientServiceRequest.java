/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import java.math.BigDecimal;

/**
 * @author rohan
 *
 */
public record ClientServiceRequest(String serviceName, String serviceDescription, int deliveryTimeDays,
		BigDecimal price, String serviceType) {
}
