package it.epicode.w6d5.devices_management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom("Devices Management API Admin <giuliomarinelli25@gmail.com>");
        msg.setSubject(subject);
        msg.setText(text);
        javaMailSender.send(msg);
    }
}
