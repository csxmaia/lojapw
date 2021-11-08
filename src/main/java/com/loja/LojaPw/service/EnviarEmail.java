package com.loja.LojaPw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EnviarEmail {
    @Autowired
    private JavaMailSender mailSender;

    public boolean send(String email, String title, String message) {
        try {
            SimpleMailMessage content = new SimpleMailMessage();
            content.setFrom("maxbluegms@gmail.com");
            content.setTo(email);
            content.setSubject(title);
            content.setText(message);
            mailSender.send(content);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
