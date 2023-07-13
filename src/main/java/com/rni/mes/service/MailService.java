package com.rni.mes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendEmail(String emailDestination, String subject, String content) throws MessagingException{
//        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
//        simpleMailMessage.setFrom("steevezguel@gmail.com");
//        simpleMailMessage.setTo(emailDestination);
//        simpleMailMessage.setSubject(subject);
//        simpleMailMessage.setText(content);
//        javaMailSender.send(simpleMailMessage);
//        simpleMailMessage.setTo("steevezguel@gmail.com");
//        javaMailSender.send(simpleMailMessage);
        
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("donaldzilag@gmail.com");
        helper.setTo(emailDestination);
        helper.setSubject(subject);
        helper.setText(content, true);
        
        javaMailSender.send(mimeMessage);
    }
}
