/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.NaturalId;

import com.Genesis.SwiftSend.Role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author rohan
 *
 */
@Data
@AllArgsConstructor
@Entity
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "client_id")
	private Integer clientId;

	private String clientName;

	@NaturalId(mutable = true)
	@Column(unique = true)
	private String email;

	@JsonIgnore // Ignore this field during serialization
	private String password;

	@JsonIgnore // Ignore this field during serialization
	private boolean isEnabled = false;

	@Column(unique = true)
	@JsonIgnore // Ignore this field during serialization
	private String mobileNumber;

	@Column(unique = true)
	private String businessNumber;

	@Column(unique = true)
	private String registryID;

	private String registeredOfficeLocation;

	private boolean isAdminApproved;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "client_role_junction", joinColumns = { @JoinColumn(name = "client_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> authorities;

	public Client() {
		super();
		authorities = new HashSet<>();
	}

}
