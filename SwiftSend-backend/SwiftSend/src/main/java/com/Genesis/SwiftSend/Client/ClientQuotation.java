/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import com.Genesis.SwiftSend.UserOrder.Orders;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @author rohan
 *
 */
@Entity
@Table(name = "client_quotations")
@Data
public class ClientQuotation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private double price;

	@Column(nullable = false)
	private int deliveryDays;

	@Column(name = "platform_fee")
	private double platformFee;

	@Column(name = "tax")
	private double tax;

	@Column(name = "total_Cost")
	private double totalCost;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Orders order;

}
