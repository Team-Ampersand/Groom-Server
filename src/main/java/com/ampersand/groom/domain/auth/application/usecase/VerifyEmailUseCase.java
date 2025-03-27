package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.application.port.AuthCodePersistencePort;
import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.domain.auth.domain.AuthCode;
import com.ampersand.groom.domain.auth.domain.Authentication;
import com.ampersand.groom.domain.auth.exception.VerificationCodeExpiredOrInvalidException;
import com.ampersand.groom.global.annotation.usecase.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class VerifyEmailUseCase {

    private final AuthCodePersistencePort authCodePersistencePort;
    private final AuthenticationPersistencePort authenticationPersistencePort;

    public void execute(String code) {
        if (!authCodePersistencePort.existsAuthCodeByCode(code)) {
            throw new VerificationCodeExpiredOrInvalidException();
        }
        AuthCode authCode = authCodePersistencePort.findAuthCodeByCode(code);
        authCodePersistencePort.deleteAuthCodeByCode(code);
        Authentication authentication = authenticationPersistencePort.findAuthenticationByEmail(authCode.getEmail());
        Authentication updatedAuth = Authentication.builder()
                .email(authCode.getEmail())
                .attemptCount(authentication.getAttemptCount())
                .verified(true)
                .ttl(authentication.getTtl())
                .build();
        authenticationPersistencePort.saveAuthentication(updatedAuth);
    }
}