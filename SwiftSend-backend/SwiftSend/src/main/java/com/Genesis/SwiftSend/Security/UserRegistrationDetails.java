/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.Security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Genesis.SwiftSend.Role.Role;
import com.Genesis.SwiftSend.User.User;

import lombok.Data;

/**
 * @author rohan
 *
 */
@Data
public class UserRegistrationDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4706734247784062505L;

	private String userName;
	private String password;
	private boolean isEnabled;
	private Set<Role> authorities;
	private String phoneNumber;
	private String emailAddress;

	public UserRegistrationDetails(User user) {
		this.userName = user.getFullName();
		this.password = user.getPassword();
		this.isEnabled = user.isEnabled(); // debug here
		this.authorities = user.getAuthorities();
		this.phoneNumber = user.getMobileNumber();
		this.emailAddress = user.getEmail();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}
}
