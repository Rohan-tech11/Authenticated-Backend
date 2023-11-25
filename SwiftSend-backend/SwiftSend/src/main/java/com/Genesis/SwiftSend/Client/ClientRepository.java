/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import java.util.List;
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

	/*
	 * // Spring Data JPA provides // a powerful // and convenient // way to //
	 * define queries // based on // method names, reducing // the need for //
	 * manual query // writing
	 */
	List<Client> findByIsAdminApprovedFalseAndIsEnabledTrue();

	List<Client> findByIsAdminApprovedTrueAndIsEnabledTrue();

}
