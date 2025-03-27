package com.ampersand.groom.domain.auth.presentation;

import com.ampersand.groom.domain.auth.application.port.AuthApplicationPort;
import com.ampersand.groom.domain.auth.presentation.data.request.*;
import com.ampersand.groom.domain.auth.presentation.data.response.AuthTokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthWebAdapter {

    private final AuthApplicationPort authApplicationPort;

    @PostMapping("/signin")
    public ResponseEntity<AuthTokenResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authApplicationPort.signIn(request.email(), request.password()));
    }

    @PostMapping("/signup")
    public void signUp(@Valid @RequestBody SignUpRequest request) {
        authApplicationPort.signUp(request.email(), request.password(), request.name());
    }

    @PutMapping("/refresh")
    public ResponseEntity<AuthTokenResponse> refreshToken(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authApplicationPort.refresh(request.refreshToken()));
    }

    @PostMapping("/verify-email")
    public void verifyEmail(@Valid @RequestBody VerificationCodeRequest request) {
        authApplicationPort.verifyEmail(request.code());
    }

    @PostMapping("/send-email")
    public void sendPasswordResetEmail(@Valid @RequestBody SendEmailRequest request) {
        authApplicationPort.sendAuthenticationEmail(request.email());
    }
}