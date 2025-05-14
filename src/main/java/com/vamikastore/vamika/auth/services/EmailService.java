package com.vamikastore.vamika.auth.services;

import com.vamikastore.vamika.auth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public String sendMail(User user) {
        String subject = "Email Verification";
        String senderName = "Vamika";
        String mailContent = "Hello" + user.getUsername() + ",\n";
        mailContent += "Your verification code is: " + user.getVerificationCode() + "\n";
        mailContent += "Please enter this code to verify your email address.\n";
        mailContent += "Thank you,\n";
        mailContent += senderName;

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(user.getEmail());
            mailMessage.setText(mailContent);
            mailMessage.setSubject(subject);
            javaMailSender.send(mailMessage);
        }
        catch (Exception e) {
            return "Error while sending email: " + e.getMessage();
        }
        return "Email sent successfully to " + user.getEmail();
    }
}

