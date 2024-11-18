package com.tejko.yamb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
public class EmailManager {

    private static final String FROM_EMAIL = "matej@jamb.com.hr";
    private static final String FROM_NAME = "Jamb";

    private final JavaMailSender mailSender;

    @Autowired
    public EmailManager(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public static String normalizeEmail(String email) {
        if (email == null) return null;
    
        String[] parts = email.toLowerCase().split("@");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid email address");
    
        String localPart = parts[0];
        String domain = parts[1];
    
        if (domain.equals("gmail.com") || domain.equals("googlemail.com")) {
            localPart = localPart.replaceAll("\\.", "");
            int plusIndex = localPart.indexOf("+");
            if (plusIndex != -1) {
                localPart = localPart.substring(0, plusIndex);
            }
        }
    
        return localPart + "@" + domain;
    }

    public void sendVerificationEmail(String to, String username, String verificationLink) {
        String htmlContent = buildContent("verification_email.html", Map.of(
                "username", username,
                "verificationLink", verificationLink
        ));
        String plainTextContent = buildContent("verification_email.txt", Map.of(
                "username", username,
                "verificationLink", verificationLink
        ));
        send(to, "Verify Your Email", htmlContent, plainTextContent);
    }

    public void sendPasswordResetEmail(String to, String username, String resetLink) {
        String htmlContent = buildContent("password_reset_email.html", Map.of(
                "username", username,
                "resetLink", resetLink
        ));
        String plainTextContent = buildContent("password_reset_email.txt", Map.of(
                "username", username,
                "resetLink", resetLink
        ));
        send(to, "Password Reset Request", htmlContent, plainTextContent);
    }

    public void sendNewUserNotificationEmail(String to, String username) {
        String htmlContent = buildContent("new_user_notification_email.html", Map.of(
                "username", username
        ));
        String plainTextContent = buildContent("new_user_notification_email.txt", Map.of(
                "username", username
        ));
        send(to, "New User: " + username, htmlContent, plainTextContent);
    }

    private void send(String to, String subject, String htmlContent, String plainTextContent) {
        try {
            MimeMessagePreparator mailMessage = mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setFrom(FROM_EMAIL, FROM_NAME);
                message.setTo(to);
                message.setSubject(subject);
                message.setText(plainTextContent, htmlContent);
            };
            mailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private String loadTemplate(String templateName) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/en/" + templateName);
        Path path = Paths.get(resource.getURI());
        return Files.readString(path);
    }

    private String buildContent(String templateName, Map<String, String> placeholders) {
        String content = "";
        try {
            content = loadTemplate(templateName);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return content;
    }

}