package org.proteored.miapeapi.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A simple email sender class.
 */
public class MailSender {
	public static final String ADMIN_MAIL_ADDRESS = "smartinez@cnb.csic.es";
	private static final String smtpServer = "127.0.0.1";
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger("log4j.logger.org.proteored");

	/**
	 * "send" method to send the message.
	 * 
	 * @throws MessagingException
	 * @throws AddressException
	 */
	private static void send(String to, String subject, String body) throws AddressException,
			MessagingException {

		Properties props = System.getProperties();
		// -- Attaching to default Session, or we could start a new one --
		props.put("mail.smtp.host", smtpServer);
		Session session = Session.getDefaultInstance(props, null);
		// -- Create a new message --
		Message msg = new MimeMessage(session);
		// -- Set the FROM and TO fields --
		msg.setFrom(new InternetAddress(ADMIN_MAIL_ADDRESS));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
		// -- We could include CC recipients too --
		// if (cc != null)
		// msg.setRecipients(Message.RecipientType.CC
		// ,InternetAddress.parse(cc, false));
		// -- Set the subject and body text --
		msg.setSubject(subject);
		msg.setText(body);
		// -- Set some other header information --
		msg.setHeader("X-Mailer", "LOTONtechEmail");
		msg.setSentDate(new Date());
		// -- Send the message --
		log.info("TRYING TO SEND THE EMAIL");
		Transport.send(msg);
		log.info("EMAIL SENT");

	}

	public static boolean sendNotificationEmail(String to, String subject, String body) {
		try {
			MailSender.send(to, subject, body);
			return true;
		} catch (AddressException e) {
			e.printStackTrace();
			log.info("Error sending email: " + e.getMessage());

		} catch (MessagingException e) {
			e.printStackTrace();
			log.info("Error sending email: " + e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error sending email: " + e.getMessage());
		}
		return false;
	}

}
