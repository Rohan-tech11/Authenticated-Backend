/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.UserOrder;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Genesis.SwiftSend.Client.Client;

/**
 * @author rohan
 *
 */
public interface OrdersRepository extends JpaRepository<Orders, Long> {
	List<Orders> findByClient(Client client);

}
