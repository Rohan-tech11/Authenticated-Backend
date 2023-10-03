/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import org.hibernate.annotations.NaturalId;

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

	// merlin check the id sequence here

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;

	private String lastName;

	@NaturalId(mutable = true)
	@Column(unique = true)
	private String email;

	private String password;

	private String role;

	private boolean isEnabled = false;

	@Column(unique = true)
	private long mobileNumber;

}