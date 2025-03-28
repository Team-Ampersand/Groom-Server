package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.application.port.AuthCodePersistencePort;
import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.domain.auth.domain.AuthCode;
import com.ampersand.groom.domain.auth.domain.Authentication;
import com.ampersand.groom.domain.auth.exception.VerificationCodeExpiredOrInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증 이메일 확인 UseCase 클래스의")
class VerifyEmailUseCaseTest {

    @Mock
    private AuthCodePersistencePort authCodePersistencePort;

    @Mock
    private AuthenticationPersistencePort authenticationPersistencePort;

    @InjectMocks
    private VerifyEmailUseCase verifyEmailUseCase;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 인증 코드일 때")
        class Context_with_valid_code {

            @Test
            @DisplayName("인증 정보를 verified=true로 업데이트한다.")
            void it_updates_authentication_to_verified() {
                // Given
                String code = "12345678";
                String email = "s00001@email.com";
                AuthCode authCode = AuthCode.builder()
                        .email(email)
                        .code(code)
                        .ttl(300L)
                        .build();
                Authentication authentication = Authentication.builder()
                        .email(email)
                        .attemptCount(1)
                        .verified(false)
                        .ttl(300L)
                        .build();
                when(authCodePersistencePort.existsAuthCodeByCode(code)).thenReturn(true);
                when(authCodePersistencePort.findAuthCodeByCode(code)).thenReturn(authCode);
                when(authenticationPersistencePort.findAuthenticationByEmail(email)).thenReturn(authentication);

                // When
                verifyEmailUseCase.execute(code);

                // Then
                verify(authCodePersistencePort).deleteAuthCodeByCode(code);
                verify(authenticationPersistencePort).saveAuthentication(argThat(updated ->
                        updated.getEmail().equals(email) &&
                                updated.getAttemptCount() == 1 &&
                                Boolean.TRUE.equals(updated.getVerified()) &&
                                updated.getTtl().equals(300L)
                ));
            }
        }

        @Nested
        @DisplayName("만료되었거나 존재하지 않는 인증 코드일 때")
        class Context_with_invalid_code {

            @Test
            @DisplayName("VerificationCodeExpiredOrInvalidException 예외를 던진다.")
            void it_throws_VerificationCodeExpiredOrInvalidException() {
                // Given
                String invalidCode = "invalid";
                when(authCodePersistencePort.existsAuthCodeByCode(invalidCode)).thenReturn(false);

                // When & Then
                assertThrows(VerificationCodeExpiredOrInvalidException.class, () ->
                        verifyEmailUseCase.execute(invalidCode)
                );
                verify(authCodePersistencePort, never()).deleteAuthCodeByCode(any());
                verify(authenticationPersistencePort, never()).saveAuthentication(any());
            }
        }
    }
}