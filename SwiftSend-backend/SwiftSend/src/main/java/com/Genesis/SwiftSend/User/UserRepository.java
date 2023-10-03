/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rohan
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

}
