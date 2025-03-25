package com.ampersand.groom.domain.auth.presentation.controller;

import com.ampersand.groom.domain.auth.application.service.AuthService;
import com.ampersand.groom.domain.auth.application.usecase.EmailVerificationUseCase;
import com.ampersand.groom.domain.auth.domain.JwtToken;
import com.ampersand.groom.domain.auth.presentation.data.request.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailVerificationUseCase emailVerificationUseCase;
    private final AuthService authService;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInRequest request) {
        JwtToken jwtToken = authService.signIn(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Signup successful");
    }

    @PatchMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody @Valid RefreshRequest request) {
        JwtToken jwtToken = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody @Valid VerificationCodeRequest request) {
        emailVerificationUseCase.executeVerifyCode(request.getCode());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Verification successful.");
    }

    @PostMapping("/signup/email")
    public ResponseEntity<?> sendSignupVerificationEmail(@RequestBody @Valid EmailRequest request) {
        emailVerificationUseCase.executeSendSignupVerificationEmail(request.getEmail());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Verification email sent");
    }

    @PostMapping("/password-change/email")
    public ResponseEntity<?> sendPasswordResetEmail(@RequestBody @Valid EmailRequest request) {
        emailVerificationUseCase.executeSendPasswordResetEmail(request.getEmail());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Verification email sent");
    }
}
