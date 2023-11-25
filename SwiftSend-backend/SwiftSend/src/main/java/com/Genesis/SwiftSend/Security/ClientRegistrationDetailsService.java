/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Genesis.SwiftSend.Client.ClientRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author rohan
 *
 */
@Service
@RequiredArgsConstructor
public class ClientRegistrationDetailsService implements UserDetailsService {

	private final ClientRepository clientRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return clientRepository.findByEmail(email).map(ClientRegistrationDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("Client not found"));
	}
}
