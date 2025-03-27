package com.ampersand.groom.domain.auth.application;

import com.ampersand.groom.domain.auth.application.port.AuthApplicationPort;
import com.ampersand.groom.domain.auth.application.usecase.*;
import com.ampersand.groom.domain.auth.presentation.data.response.AuthTokenResponse;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter(AdapterType.INBOUND)
public class AuthApplicationAdapter implements AuthApplicationPort {

    private final SignUpUseCase signUpUseCase;
    private final SignInUseCase signInUseCase;
    private final RefreshUseCase refreshUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final SendAuthenticationEmailUseCase sendAuthenticationEmailUseCase;

    @Override
    public void signUp(String email, String password, String name) {
        signUpUseCase.execute(email, password, name);
    }

    @Override
    public AuthTokenResponse signIn(String username, String password) {
        return signInUseCase.execute(username, password);
    }

    @Override
    public AuthTokenResponse refresh(String refreshToken) {
        return refreshUseCase.execute(refreshToken);
    }

    @Override
    public void verifyEmail(String code) {
        verifyEmailUseCase.execute(code);
    }

    @Override
    public void sendAuthenticationEmail(String email) {
        sendAuthenticationEmailUseCase.execute(email);
    }
}
