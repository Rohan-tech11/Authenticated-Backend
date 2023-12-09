/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.UserOrder;

import com.Genesis.SwiftSend.Client.ClientServices;
import com.Genesis.SwiftSend.User.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rohan
 *
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "user_orders")
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String source;

	@Column(nullable = false)
	private String destination;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "client_services_id")
	private ClientServices clientServices;

	@Column(nullable = false)
	private String premium;

	@Column(nullable = false)
	private String dimensions;

	@Column(nullable = false)
	private double weight;

	@Column(nullable = false)
	private String type;

	@Column
	private String imageURL;

	// Constructors, getters, and setters

}
