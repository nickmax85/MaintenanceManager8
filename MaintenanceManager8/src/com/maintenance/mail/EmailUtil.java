package com.maintenance.mail;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailUtil {

	public static void main(String[] args) {

		EmailUtil mail = new EmailUtil();

		mail.test();

	}

	private void test() {

		String smtpHostServer = "smtp-auth.magna.global";
		String from = "svc_LIAT_Maintenance@magna.com";

		String to = "markus.thaler@magna.com, markus.thaler@gmx.at";
		String betreff = "Betreff";
		String text = "Diese Nachricht wurde an folgende Adressen versendet: " + to;

		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHostServer);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");

		//create Authenticator object to pass in Session.getInstance argument
				Authenticator auth = new Authenticator() {
					//override the getPasswordAuthentication method
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, "@VdvCxkoauXdhhz1");
					}
				};
		
		Session session = Session.getInstance(props, auth);
		session.setDebug(true);

		try {
			sendEmail(session, from, to, null, betreff, text, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param session
	 * @param to
	 * @param betreff
	 * @param text
	 */
	public static void sendEmail(Session session, String from, String to, String cc, String betreff, String text,
			List<File> files) throws Exception {

		MimeMessage msg = new MimeMessage(session);

		// set message headers
		msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
		msg.addHeader("format", "flowed");
		msg.addHeader("Content-Transfer-Encoding", "8bit");

		msg.setFrom(new InternetAddress(from, from));

		// msg.setReplyTo(InternetAddress.parse("no_reply@journaldev.com", false));

		msg.setSubject(betreff, "UTF-8");

		msg.setText(text, "UTF-8");

		msg.setSentDate(new Date());

		msg.setRecipients(Message.RecipientType.TO, to);

		// if (!cc.isEmpty())
		// msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc,
		// false));

		// Create the message part
		BodyPart messageBodyPart = new MimeBodyPart();

		// Fill the message
		messageBodyPart.setText(text);

		// Create a multipar message
		Multipart multipart = new MimeMultipart();

		// Set text message part
		multipart.addBodyPart(messageBodyPart);

		// Part two is attachment
		if (files != null) {

			for (File file : files) {
				messageBodyPart = new MimeBodyPart();
				String filename = file.getAbsolutePath();
				DataSource source = new FileDataSource(filename);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(file.getName());
				multipart.addBodyPart(messageBodyPart);
			}
		}

		// Send the complete message parts
		msg.setContent(multipart);

		System.out.println("Nachricht ist bereit");

		Transport.send(msg);

		System.out.println("Nachricht wurde erfolgreich versendet");

	}

}
