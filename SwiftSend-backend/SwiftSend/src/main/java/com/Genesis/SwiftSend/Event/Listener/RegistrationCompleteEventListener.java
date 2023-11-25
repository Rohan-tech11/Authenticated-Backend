/**
 * Created by Rohan
 */

package com.Genesis.SwiftSend.Event.Listener;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.Genesis.SwiftSend.Client.Client;
import com.Genesis.SwiftSend.Client.ClientService;
import com.Genesis.SwiftSend.Event.RegistrationCompleteEvent;
import com.Genesis.SwiftSend.User.User;
import com.Genesis.SwiftSend.User.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rohan
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

	private final UserService userService;
	private final ClientService clientService;

	private final JavaMailSender mailSender;
	private User theUser;
	private Client theClient;

	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		// 1. Get the newly registered user
		theUser = event.getUser();
		theClient = event.getClient();
		// 2. Create a verification token for the user
		String verificationToken = UUID.randomUUID().toString();
		// 4 Build the verification url to be sent to the user
		String url = event.getApplicationUrl() + "/api/register/verifyEmail?token=" + verificationToken;
		// 5. Send the email.

		try {
			if (theUser == null) {
				clientService.saveClientVerificationToken(theClient, verificationToken);
				sendVerificationEmailtoClient(url);
			} else {
				// 3. Save the verification token for the user
				userService.saveUserVerificationToken(theUser, verificationToken);
				sendVerificationEmail(url);

			}
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		log.info("Click the link to verify your registration :  {}", url);
	}

	public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
		String subject = "Email Verification";
		String senderName = "SwiftSend Registration Portal Service";
		String mailContent = "<p> Hi, " + theUser.getFullName() + ", </p>" + "<p>Thank you for registering with us,"
				+ "" + "Please, follow the link below to complete your registration.</p>" + "<a href=\"" + url
				+ "\">Verify your email to activate your account</a>"
				+ "<p> Thank you <br> Swiftsend users Registration Portal Service";
		MimeMessage message = mailSender.createMimeMessage();
		var messageHelper = new MimeMessageHelper(message);
		messageHelper.setFrom("heyminion0401@gmail.com", senderName);
		messageHelper.setTo(theUser.getEmail());
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}

	public void sendVerificationEmailtoClient(String url) throws MessagingException, UnsupportedEncodingException {
		String subject = "Email Verification for the Cllient";
		String senderName = "SwiftSend Registration  Client Portal Service";
		String mailContent = "<p> Hi, " + theClient.getClientName() + ", </p>" + "<p>Thank you for registering with us,"
				+ "" + "Please, follow the link below to complete your registration.</p>" + "<a href=\"" + url
				+ "\">Verify your email to activate your account, you must verify your email to  get approval from admim</a>"
				+ "<p> Thank you <br> Swiftsend users Registration Portal Service";
		MimeMessage message = mailSender.createMimeMessage();
		var messageHelper = new MimeMessageHelper(message);
		messageHelper.setFrom("heyminion0401@gmail.com", senderName);
		messageHelper.setTo(theClient.getEmail());
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}
}
