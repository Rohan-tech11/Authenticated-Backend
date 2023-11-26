/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "client_services")
@Data
public class ClientServices {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	@JsonIgnore
	private Client client;

	@Column(name = "service_name", nullable = false)
	private String serviceName;

	@Column(name = "service_description")
	private String serviceDescription;

	@Column(name = "delivery_time_days")
	private int deliveryTimeDays;

	@Column(name = "price", precision = 10, scale = 2)
	private BigDecimal price;

}