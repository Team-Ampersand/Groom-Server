package com.ampersand.groom.domain.auth.application.port;

import com.ampersand.groom.domain.auth.presentation.data.response.AuthTokenResponse;

public interface AuthApplicationPort {

    void signUp(String email, String password, String name);

    AuthTokenResponse signIn(String username, String password);

    AuthTokenResponse refresh(String refreshToken);

    void verifyEmail(String code);

    void sendAuthenticationEmail(String email);
}