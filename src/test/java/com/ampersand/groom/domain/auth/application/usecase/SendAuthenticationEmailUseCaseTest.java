package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.domain.AuthCode;
import com.ampersand.groom.domain.auth.domain.Authentication;
import com.ampersand.groom.domain.auth.exception.EmailAuthRateLimitException;
import com.ampersand.groom.domain.auth.application.port.AuthCodePersistencePort;
import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.infrastructure.thirdparty.email.service.EmailSendService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("인증 이메일 전송 UseCase 클래스의")
class SendAuthenticationEmailUseCaseTest {

    @Mock
    private AuthCodePersistencePort authCodePersistencePort;

    @Mock
    private AuthenticationPersistencePort authenticationPersistencePort;

    @Mock
    private EmailSendService emailSendService;

    @InjectMocks
    private SendAuthenticationEmailUseCase sendAuthenticationEmailUseCase;

    private final String email = "s00001@email.com";
    private final long ttl = 300L;
    private final int attemptLimit = 3;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sendAuthenticationEmailUseCase, "ttl", ttl);
        ReflectionTestUtils.setField(sendAuthenticationEmailUseCase, "attemptCountLimit", attemptLimit);
    }

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("처음 인증을 요청할 때")
        class Context_with_first_auth_request {

            @Test
            @DisplayName("인증 객체를 저장하고 인증 코드를 전송한다.")
            void it_saves_new_authentication_and_sends_code() throws MessagingException {
                // Given
                when(authenticationPersistencePort.existsAuthenticationByEmail(email)).thenReturn(false);

                // When
                sendAuthenticationEmailUseCase.execute(email);

                // Then
                verify(authenticationPersistencePort).saveAuthentication(argThat(auth ->
                        auth.getEmail().equals(email) &&
                                auth.getAttemptCount() == 1 &&
                                Boolean.FALSE.equals(auth.getVerified()) &&
                                auth.getTtl().equals(ttl)
                ));
                verify(authCodePersistencePort).saveAuthCode(any(AuthCode.class));
                verify(emailSendService).sendMail(eq(email), anyString());
            }
        }

        @Nested
        @DisplayName("인증 요청이 이미 존재할 때")
        class Context_with_existing_authentication {

            @Test
            @DisplayName("시도 횟수가 초과되지 않았다면 업데이트 후 이메일을 전송한다.")
            void it_updates_authentication_and_sends_code() throws MessagingException {
                // Given
                Authentication existing = Authentication.builder()
                        .email(email)
                        .attemptCount(1)
                        .verified(false)
                        .ttl(ttl)
                        .build();
                when(authenticationPersistencePort.existsAuthenticationByEmail(email)).thenReturn(true);
                when(authenticationPersistencePort.findAuthenticationByEmail(email)).thenReturn(existing);

                // When
                sendAuthenticationEmailUseCase.execute(email);

                // Then
                verify(authenticationPersistencePort).saveAuthentication(argThat(auth ->
                        auth.getAttemptCount() == 2
                ));
                verify(authCodePersistencePort).saveAuthCode(any(AuthCode.class));
                verify(emailSendService).sendMail(eq(email), anyString());
            }

            @Test
            @DisplayName("시도 횟수가 초과되었다면 예외를 던진다.")
            void it_throws_EmailAuthRateLimitException() throws MessagingException {
                // Given
                Authentication rateLimited = Authentication.builder()
                        .email(email)
                        .attemptCount(attemptLimit)
                        .verified(false)
                        .ttl(ttl)
                        .build();
                when(authenticationPersistencePort.existsAuthenticationByEmail(email)).thenReturn(true);
                when(authenticationPersistencePort.findAuthenticationByEmail(email)).thenReturn(rateLimited);

                // When & Then
                assertThrows(EmailAuthRateLimitException.class, () ->
                        sendAuthenticationEmailUseCase.execute(email)
                );
                verify(authCodePersistencePort, never()).saveAuthCode(any());
                verify(emailSendService, never()).sendMail(any(), any());
            }
        }
    }
}