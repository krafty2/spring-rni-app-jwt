package com.rni.mes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendEmail(String emailDestination, String subject, String content){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom("steevezguel@gmail.com");
        simpleMailMessage.setTo(emailDestination);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        javaMailSender.send(simpleMailMessage);
        simpleMailMessage.setTo("steevezguel@gmail.com");
        javaMailSender.send(simpleMailMessage);
    }
}
