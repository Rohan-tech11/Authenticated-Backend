/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rohan
 *
 */
public interface ClientRepository extends JpaRepository<Client, Integer> {

	Optional<Client> findByMobileNumber(String mobileNumber);

	/**
	 * @param email
	 * @return
	 */
	Optional<Client> findByEmail(String email);

	/**
	 * @param businessNumber
	 * @return
	 */
	Optional<Client> findByBusinessNumber(String businessNumber);

	/**
	 * @param registryID
	 * @return
	 */
	Optional<Client> findByRegistryID(String registryID);

}
