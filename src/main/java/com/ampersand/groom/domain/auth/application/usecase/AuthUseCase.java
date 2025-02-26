package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.application.service.AuthService;
import com.ampersand.groom.domain.auth.domain.JwtToken;
import com.ampersand.groom.domain.auth.presentation.data.Request.SignupRequest;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithTransaction;
import lombok.RequiredArgsConstructor;

@UseCaseWithTransaction
@RequiredArgsConstructor
public class AuthUseCase {

    private final AuthService authService;

    public JwtToken executeSignIn(String email, String password) {
        return authService.signIn(email, password);
    }

    public void executeSignup(SignupRequest request) {
        authService.signup(request);
    }

    public JwtToken executeRefreshToken(String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
}
