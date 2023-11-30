/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rohan
 *
 */

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByAuthority(String authority);
}