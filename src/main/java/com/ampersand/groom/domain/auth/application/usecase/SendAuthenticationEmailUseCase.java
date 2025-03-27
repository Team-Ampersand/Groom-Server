package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.application.port.AuthCodePersistencePort;
import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.domain.auth.application.util.GenerationAuthCode;
import com.ampersand.groom.domain.auth.domain.AuthCode;
import com.ampersand.groom.domain.auth.domain.Authentication;
import com.ampersand.groom.domain.auth.exception.EmailAuthRateLimitException;
import com.ampersand.groom.global.annotation.usecase.UseCase;
import com.ampersand.groom.infrastructure.thirdparty.email.service.EmailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@UseCase
@RequiredArgsConstructor
public class SendAuthenticationEmailUseCase {

    private final AuthCodePersistencePort authCodePersistencePort;
    private final AuthenticationPersistencePort authenticationPersistencePort;
    private final EmailSendService emailSendService;
    @Value("${email.authentication.object.ttl}")
    private Long ttl;
    @Value("${email.authentication.object.attempt-count-limit}")
    private Integer attemptCountLimit;

    public void execute(String email) {
        if (authenticationPersistencePort.existsAuthenticationByEmail(email)) {
            Authentication authentication = authenticationPersistencePort.findAuthenticationByEmail(email);
            if (authentication.getAttemptCount() >= attemptCountLimit) {
                throw new EmailAuthRateLimitException();
            }
            Authentication updatedAuthentication = Authentication.builder()
                    .email(authentication.getEmail())
                    .attemptCount(authentication.getAttemptCount() + 1)
                    .verified(authentication.getVerified())
                    .ttl(authentication.getTtl())
                    .build();
            authenticationPersistencePort.saveAuthentication(updatedAuthentication);
        } else {
            Authentication newAuthentication = Authentication.builder()
                    .email(email)
                    .attemptCount(1)
                    .verified(false)
                    .ttl(ttl)
                    .build();
            authenticationPersistencePort.saveAuthentication(newAuthentication);
        }
        AuthCode authCode = AuthCode.builder()
                .email(email)
                .code(GenerationAuthCode.generateAuthCode())
                .ttl(ttl)
                .build();
        authCodePersistencePort.saveAuthCode(authCode);
        emailSendService.sendMail(email, authCode.getCode());
    }
}