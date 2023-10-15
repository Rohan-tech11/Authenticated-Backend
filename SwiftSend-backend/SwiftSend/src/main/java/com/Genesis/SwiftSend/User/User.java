/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rohan
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore // Ignore this field during serialization
	private Long id;

	private String fullName;

	@NaturalId(mutable = true)
	@Column(unique = true)
	private String email;

	@JsonIgnore // Ignore this field during serialization
	private String password;

	@JsonIgnore // Ignore this field during serialization
	private String role;

	@JsonIgnore // Ignore this field during serialization
	private boolean isEnabled = false;

	@Column(unique = true)
	@JsonIgnore // Ignore this field during serialization
	private String mobileNumber;

}
