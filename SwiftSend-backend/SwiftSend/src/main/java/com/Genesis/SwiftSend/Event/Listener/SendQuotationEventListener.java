/**
 * created by @Rohan
 */
package com.Genesis.SwiftSend.Event.Listener;

import java.io.UnsupportedEncodingException;

import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.Genesis.SwiftSend.Client.ClientQuotation;
import com.Genesis.SwiftSend.Event.SendQuotationEvent;
import com.Genesis.SwiftSend.User.User;

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
public class SendQuotationEventListener implements ApplicationListener<SendQuotationEvent> {
	private User theUser;
	private ClientQuotation clientQuotation;
	private final JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(SendQuotationEvent event) {
		theUser = event.getUser();
		log.info(theUser.getEmail() + "user email addreess is ");

		clientQuotation = event.getClientQuotation();
		try {
			if (theUser != null)
				sendVerificationEmail(theUser.getEmail());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendVerificationEmail(String email) throws MessagingException, UnsupportedEncodingException {
		String subject = "Quotation for Your Recent Order Verification";
		String senderName = "SwiftSend Quotation Portal Service";
		log.info("inside verification email");
		String mailContent = "<p> Hi, " + theUser.getFullName() + ", </p>"
				+ "<p>Thank you for placing an order with us. Here is your quotation:</p>" + "<p>Client: "
				+ clientQuotation.getClient().getBusinessName() + "</p>" + "<p>Delivery Days: "
				+ clientQuotation.getDeliveryDays() + "</p>" + "<p>Price: " + clientQuotation.getPrice() + "</p>"
				+ "<p>PlatformFees: " + clientQuotation.getPlatformFee() + "</p>" + "<p>Tax: "
				+ clientQuotation.getTax() + "</p>" + "<p>Thank you for choosing SwiftSend!</p>" + "<p>Total Cost: "
				+ clientQuotation.getTotalCost() + "</p>" + "<p> Please Check your Dashboard For More Action ! </p>";

		MimeMessage message = mailSender.createMimeMessage();
		var messageHelper = new MimeMessageHelper(message);
		messageHelper.setFrom("swiftsend743@gmail.com", senderName);
		messageHelper.setTo(theUser.getEmail());
		messageHelper.setSubject(subject);
		messageHelper.setText(mailContent, true);
		mailSender.send(message);
	}

}
