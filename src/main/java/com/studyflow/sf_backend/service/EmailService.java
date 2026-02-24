package com.studyflow.sf_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private JavaMailSender mailSender;

    /**
     * Sends a verification code to the user's email address.
     *
     * @param toEmail the recipient email address
     * @param verificationCode the code to send (valid for 10 minutes)
     */
    public void sendVerificationCode(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verification Code");
        message.setText(verificationCode + " Valid for 10 minutes.");
        mailSender.send(message);
    }
}
