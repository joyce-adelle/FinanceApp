package com.trove.project.services.implementations;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.trove.project.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private static final String NOREPLY_ADDRESS = "financeapp<noreply@financeapp.com>";

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private TemplateEngine thymeleafTemplateEngine;

	@Override
	public void sendMessage(String to, String subject, Map<String, Object> templateModel, String template)
			throws MessagingException {

		Context thymeleafContext = new Context();
		thymeleafContext.setVariables(templateModel);

		String htmlBody = thymeleafTemplateEngine.process("emails/" + template + ".html", thymeleafContext);

		sendHtmlMessage(to, subject, htmlBody);
	}

	private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom(new InternetAddress(NOREPLY_ADDRESS));
		helper.setTo(new InternetAddress(to));
		helper.setSubject(subject);
		helper.setText(htmlBody, true);
		emailSender.send(message);
	}

}