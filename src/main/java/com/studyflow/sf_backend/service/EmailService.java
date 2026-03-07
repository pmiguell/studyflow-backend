package com.studyflow.sf_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    /**
     * Sends a verification code to the user's email address.
     *
     * @param toEmail the recipient email address
     * @param verificationCode the code to send (valid for 10 minutes)
     */
    public void sendVerificationCode(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Verification Code for StudyFlow");
        message.setText("Welcome to StudyFlow! Your verification code is: " + verificationCode + "\n\nValid for 10 minutes.");
        mailSender.send(message);
    }

    /**
     * Sends a password recovery code to the user's email address.
     *
     * @param toEmail the recipient email address
     * @param recoveryCode the code to send (valid for 10 minutes)
     */
    public void sendPasswordRecoveryCode(String toEmail, String recoveryCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Recovery for StudyFlow");
        message.setText("We received a request to recover your password.\nYour recovery code is: " + recoveryCode + "\n\nIf you did not request this, please ignore this email. Valid for 10 minutes.");
        mailSender.send(message);
    }
}
