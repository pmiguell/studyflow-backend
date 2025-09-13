package com.studyflow.sf_backend.dto;

public record VerifyUserRequest(String email, String verificationCode) {
}
