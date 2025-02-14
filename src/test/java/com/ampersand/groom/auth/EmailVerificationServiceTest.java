package com.ampersand.groom.auth;

import com.ampersand.groom.domain.auth.application.port.EmailVerificationPort;
import com.ampersand.groom.domain.auth.application.service.EmailVerificationService;
import com.ampersand.groom.domain.auth.domain.model.EmailVerification;
import com.ampersand.groom.domain.auth.expection.AuthException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailVerificationService 클래스의")
class EmailVerificationServiceTest {

    @Mock
    private EmailVerificationPort emailVerificationPort;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailVerificationService emailVerificationService;

    @DisplayName("sendSignupVerificationEmail 메서드는")
    @Nested
    class Describe_sendSignupVerificationEmail {

        @Nested
        @DisplayName("회원가입 인증 이메일 전송 시")
        class Context_with_signup_verification_email {

            @Test
            @DisplayName("이메일을 전송하고 인증 정보를 저장한다.")
            void it_sends_email_and_saves_verification() {
                // given
                EmailVerification emailVerification = new EmailVerification("test@example.com", "123456");
                when(emailVerificationPort.save(any(EmailVerification.class))).thenReturn(emailVerification);

                // when
                emailVerificationService.sendSignupVerificationEmail("test@example.com");

                // then
                verify(javaMailSender).send(any(SimpleMailMessage.class));  // 이메일이 전송되었는지 확인
                verify(emailVerificationPort).save(any(EmailVerification.class));  // 인증 정보가 저장되었는지 확인
            }
        }
    }

    @DisplayName("verifyEmailCode 메서드는")
    @Nested
    class Describe_verifyEmailCode {

        @Nested
        @DisplayName("유효한 인증 코드일 때")
        class Context_with_valid_code {

            @Test
            @DisplayName("인증을 완료하고 true를 반환한다.")
            void it_verifies_code_and_returns_true() {
                // given
                EmailVerification emailVerification = new EmailVerification("test@example.com", "123456");
                when(emailVerificationPort.findByCode("123456")).thenReturn(Optional.of(emailVerification));
                when(emailVerificationPort.save(any(EmailVerification.class))).thenReturn(emailVerification);

                // when
                boolean result = emailVerificationService.verifyEmailCode("123456");

                // then
                assertTrue(result);  // 반환값이 true인지를 확인
                assertTrue(emailVerification.isVerified());  // 인증 객체의 verified가 true로 설정되었는지 확인
            }
        }

        @Nested
        @DisplayName("유효하지 않거나 만료된 인증 코드일 때")
        class Context_with_invalid_or_expired_code {

            @Test
            @DisplayName("AuthException 예외를 던진다.")
            void it_throws_AuthException() {
                // given
                when(emailVerificationPort.findByCode("123456")).thenReturn(Optional.empty());

                // when & then
                AuthException exception = assertThrows(AuthException.class, () ->
                        emailVerificationService.verifyEmailCode("123456")
                );
                assertEquals("Invalid or expired verification code", exception.getMessage());  // 예외 메시지 확인
            }
        }
    }

    @DisplayName("deleteExpiredVerifications 메서드는")
    @Nested
    class Describe_deleteExpiredVerifications {

        @Test
        @DisplayName("만료된 인증 정보를 삭제한다.")
        void it_deletes_expired_verifications() {
            // given
            doNothing().when(emailVerificationPort).deleteAllExpired(any());  // 반환값 없이 호출만 설정

            // when
            emailVerificationService.deleteExpiredVerifications();

            // then
            verify(emailVerificationPort).deleteAllExpired(any());  // 메서드가 호출되었는지 확인
        }
    }
}
