/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

/**
 * @author rohan
 *
 */
//Java record to represent the request body
public record QuotationRequest(int deliveryDays, double price) {
	// Any additional annotations or methods can be added as needed
}
