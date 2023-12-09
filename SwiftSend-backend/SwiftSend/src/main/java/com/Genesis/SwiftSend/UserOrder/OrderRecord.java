/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.UserOrder;

/**
 * @author rohan
 *
 */
public record OrderRecord(String source, String destination, String premium, String dimensions, double weight,
		String type, String imageUrl) {

}
