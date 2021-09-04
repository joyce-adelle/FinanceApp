package com.trove.project.services;

import java.util.Map;

import javax.mail.MessagingException;

public interface EmailService {

	void sendMessage(String to, String subject, Map<String, Object> templateModel, String template)
			throws MessagingException;

}
