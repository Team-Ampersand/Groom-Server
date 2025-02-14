package com.ampersand.groom.domain.auth.presentation.controller;

import com.ampersand.groom.domain.auth.application.usecase.EmailVerificationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailVerificationUseCase emailVerificationUseCase;

    @PostMapping("/email/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, Object> body) {
        String code = (String) body.get("code");

        if (code == null || code.length() != 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code format.");
        }

        boolean result = emailVerificationUseCase.executeVerifyEmail(code);
        if (result) {
            return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Verification successful.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired verification code");
        }
    }

    @PostMapping("/email/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");

        if (email == null || email.length() > 16) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format.");
        }

        emailVerificationUseCase.executeSendSignupVerificationEmail(email);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Verification email sent");
    }

    @PostMapping("/email/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");

        if (email == null || email.length() > 16) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format.");
        }

        emailVerificationUseCase.executeSendPasswordResetEmail(email);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Verification email sent");
    }
}