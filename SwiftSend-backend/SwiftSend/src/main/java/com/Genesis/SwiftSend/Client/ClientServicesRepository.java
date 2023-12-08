/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Client;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rohan
 *
 */
public interface ClientServicesRepository extends JpaRepository<ClientServices, Long> {

//	 and the ClientServices table has a foreign key reference to the Client table
	// data dpa will automatically fcreate the queries using entity relationships
	// @Query("SELECT cs FROM ClientServices cs JOIN FETCH cs.client c WHERE c.email
	// = :clientEmail")
	List<ClientServices> findByClientEmail(String clientEmail);
}
