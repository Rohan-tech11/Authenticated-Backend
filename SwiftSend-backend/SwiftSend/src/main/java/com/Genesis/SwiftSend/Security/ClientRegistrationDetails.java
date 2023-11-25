/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.Security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Genesis.SwiftSend.Client.Client;
import com.Genesis.SwiftSend.Role.Role;

import lombok.Data;

/**
 * @author rohan
 *
 */
@Data
public class ClientRegistrationDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4706734247784062505L;

	private String userName;
	private String password;
	private boolean isEnabled;
	private Set<Role> authorities;

	public ClientRegistrationDetails(Client client) {
		this.userName = client.getEmail();
		this.password = client.getPassword();
		this.isEnabled = client.isEnabled(); // enabled will change to true if client did the email verify
		this.authorities = client.getAuthorities();
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
