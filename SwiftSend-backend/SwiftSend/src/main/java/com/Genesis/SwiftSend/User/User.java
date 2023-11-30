/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

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
 * @author Rohan
 *
 */
@Data
@AllArgsConstructor
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	@JsonIgnore // Ignore this field during serialization
	private Integer userId;

	private String fullName;

	@NaturalId(mutable = true)
	@Column(unique = true)
	private String email;

	@JsonIgnore // Ignore this field during serialization
	private String password;

//	@JsonIgnore // Ignore this field during serialization
//	private String role;

	@JsonIgnore // Ignore this field during serialization
	private boolean isEnabled = false;

	@Column(unique = true)
	@JsonIgnore // Ignore this field during serialization
	private String mobileNumber;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role_junction", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> authorities;

	public User() {
		super();
		authorities = new HashSet<>();
	}

}
