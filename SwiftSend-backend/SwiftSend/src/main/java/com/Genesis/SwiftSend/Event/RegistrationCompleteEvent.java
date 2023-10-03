/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.Event;

import org.springframework.context.ApplicationEvent;

import com.Genesis.SwiftSend.User.User;

import lombok.Getter;
import lombok.Setter;

/**
 * @author rohan
 *
 */
@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8973004308630404367L;
	private User user;
	private String applicationUrl;

	public RegistrationCompleteEvent(User user, String applicationUrl) {
		super(user);
		this.user = user;
		this.applicationUrl = applicationUrl;
	}
}