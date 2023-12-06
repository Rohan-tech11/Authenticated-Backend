/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Admin;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.Genesis.SwiftSend.Client.Client;

/**
 * @author rohan
 *
 */
public interface IAdminService {

	List<Client> getAllClientAccounts();

	/**
	 * @return
	 */
	List<Client> getAllUnapprovedClientAccounts();

	/**
	 * @return
	 */
	List<Client> getAllapprovedClientAccounts();

	/**
	 * @param clientId
	 * @return
	 */
	ResponseEntity<Object> approveClientAccount(Integer clientId);

	/**
	 * @return
	 */
	List<Client> getAllUnVerifiedClientAccounts();

	/**
	 * @return
	 */
	List<Client> getAllRegisteredClientAccounts();

	/**
	 * @return
	 */
}
