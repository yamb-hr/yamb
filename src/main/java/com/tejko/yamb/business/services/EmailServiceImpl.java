package com.tejko.yamb.business.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final String fromName;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.fromEmail = "matej@jamb.com.hr";
        this.fromName = "Jamb";
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        MimeMessagePreparator mailMessage = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(fromEmail, fromName);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
        };
        mailSender.send(mailMessage);
    }
}
