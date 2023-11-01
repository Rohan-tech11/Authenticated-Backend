/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Genesis.SwiftSend.User.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author rohan
 *
 */
@Service
@RequiredArgsConstructor
public class UserRegistrationDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email).map(UserRegistrationDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
}
