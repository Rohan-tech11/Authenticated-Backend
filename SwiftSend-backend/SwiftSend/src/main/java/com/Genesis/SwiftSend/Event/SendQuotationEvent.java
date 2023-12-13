/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Event;

import org.springframework.context.ApplicationEvent;

import com.Genesis.SwiftSend.Client.ClientQuotation;
import com.Genesis.SwiftSend.User.User;

import lombok.Getter;
import lombok.Setter;

/**
 * @author rohan
 *
 */
@Getter
@Setter
public class SendQuotationEvent extends ApplicationEvent {

	private User user;
	private ClientQuotation clientQuotation;

	public SendQuotationEvent(User user, ClientQuotation clientQuotation) {
		super(user);
		this.user = user;
		this.clientQuotation = clientQuotation;
	}

}
