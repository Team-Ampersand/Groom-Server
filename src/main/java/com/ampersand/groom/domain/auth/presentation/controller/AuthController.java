package com.ampersand.groom.domain.auth.presentation.controller;

import com.ampersand.groom.domain.auth.application.usecase.EmailVerificationUseCase;
import com.ampersand.groom.domain.auth.presentation.dto.EmailRequest;
import com.ampersand.groom.domain.auth.presentation.dto.VerificationCodeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailVerificationUseCase emailVerificationUseCase;

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid VerificationCodeRequest request) {
        emailVerificationUseCase.executeVerifyCode(request.getCode());
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Verification successful.");
    }

    @PostMapping("/signup/email")
    public ResponseEntity<?> signup(@RequestBody @Valid EmailRequest request) {
        emailVerificationUseCase.executeSendSignupVerificationEmail(request.getEmail());
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Verification email sent");
    }

    @PostMapping("/password-change/email")
    public ResponseEntity<?> refresh(@RequestBody @Valid EmailRequest request) {
        emailVerificationUseCase.executeSendPasswordResetEmail(request.getEmail());
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Verification email sent");
    }
}