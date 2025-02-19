package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.application.service.EmailVerificationService;
import com.ampersand.groom.domain.auth.expection.EmailFormatInvalidException;
import com.ampersand.groom.domain.auth.expection.VerificationCodeFormatInvalidException;
import com.ampersand.groom.domain.auth.expection.VerificationCodeExpiredOrInvalidException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailVerificationUseCase 클래스의")
class EmailVerificationUseCaseTest {

    @Mock
    private EmailVerificationService emailVerificationService;

    @InjectMocks
    private EmailVerificationUseCase emailVerificationUseCase;

    @DisplayName("executeSendSignupVerificationEmail 메서드는")
    @Nested
    class Describe_executeSendSignupVerificationEmail {

        @Nested
        @DisplayName("유효한 이메일을 입력했을 때")
        class Context_with_valid_email {

            @Test
            @DisplayName("회원가입 인증 이메일을 전송한다.")
            void it_sends_signup_verification_email() {
                // given
                String email = "test@example.com";

                // when
                emailVerificationUseCase.executeSendSignupVerificationEmail(email);

                // then
                verify(emailVerificationService).sendSignupVerificationEmail(email); // 메서드 호출 검증
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이메일을 입력했을 때")
        class Context_with_invalid_email {

            @Test
            @DisplayName("InvalidFormatException을 발생시킨다.")
            void it_throws_invalid_format_exception() {
                // given
                String invalidEmail = "email"; // 잘못된 이메일 주소


                // when
                doThrow(new EmailFormatInvalidException())
                        .when(emailVerificationService).sendSignupVerificationEmail(invalidEmail);


                EmailFormatInvalidException exception = assertThrows(EmailFormatInvalidException.class, () -> {
                    emailVerificationUseCase.executeSendSignupVerificationEmail(invalidEmail); // 이메일 전송 시 예외 발생해야 함
                });

                // then
                assertEquals("Email format invalid", exception.getErrorCode().getMessage()); // 예외 메시지 확인
                assertEquals(400, exception.getErrorCode().getHttpStatus()); // HTTP 상태 코드 확인
            }

        }

        @DisplayName("executeSendPasswordResetEmail 메서드는")
        @Nested
        class Describe_executeSendPasswordResetEmail {

            @Nested
            @DisplayName("유효한 이메일을 입력했을 때")
            class Context_with_valid_email {

                @Test
                @DisplayName("비밀번호 변경 인증 이메일을 전송한다.")
                void it_sends_password_reset_email() {
                    // given
                    String email = "s24010@gsm.hs.kr";

                    // when
                    emailVerificationUseCase.executeSendPasswordResetEmail(email);

                    // then
                    verify(emailVerificationService).sendPasswordResetEmail(email); // 메서드 호출 검증
                }
            }

            @Nested
            @DisplayName("유효하지 않은 이메일을 입력했을 때")
            class Context_with_invalid_email {

                @Test
                @DisplayName("InvalidFormatException을 발생시킨다.")
                void it_throws_invalid_format_exception() {
                    // given
                    String invalidEmail = "email"; // 잘못된 이메일 주소

                    // when
                    doThrow(new EmailFormatInvalidException())
                            .when(emailVerificationService).sendPasswordResetEmail(invalidEmail);

                    EmailFormatInvalidException exception = assertThrows(EmailFormatInvalidException.class, () -> {
                        emailVerificationUseCase.executeSendPasswordResetEmail(invalidEmail); // 이메일 전송 시 예외 발생해야 함
                    });

                    // then
                    assertEquals("Email format invalid", exception.getErrorCode().getMessage());
                    assertEquals(400, exception.getErrorCode().getHttpStatus());
                }

            }
        }

        @DisplayName("executeVerifyCode 메서드는")
        @Nested
        class Describe_executeVerifyCode {

            @Nested
            @DisplayName("유효하지 않거나 만료된 인증 코드를 입력했을 때")
            class Context_with_invalid_or_expired_code {

                @Test
                @DisplayName("InvalidOrExpiredCodeException을 발생시킨다.")
                void it_throws_invalid_or_expired_code_exception() {
                    // given
                    String invalidCode = "12345678";

                    // when
                    doThrow(new VerificationCodeExpiredOrInvalidException())
                            .when(emailVerificationService).verifyCode(invalidCode); // doThrow 사용

                    // then
                    VerificationCodeExpiredOrInvalidException exception = assertThrows(VerificationCodeExpiredOrInvalidException.class, () -> {
                        emailVerificationUseCase.executeVerifyCode(invalidCode);
                    });

                    assertEquals("Verification code expired or invalid", exception.getErrorCode().getMessage());
                    assertEquals(401, exception.getErrorCode().getHttpStatus());
                }
            }

            @Nested
            @DisplayName("올바르지 않은 형식의 코드를 입력했을 때")
            class Context_with_invalid_code_format {

                @Test
                @DisplayName("InvalidFormatException을 발생시킨다.")
                void it_throws_invalid_format_exception() {
                    String invalidCode = "123456";

                    doThrow(new VerificationCodeFormatInvalidException())
                            .when(emailVerificationService).verifyCode(invalidCode);

                    VerificationCodeFormatInvalidException exception = assertThrows(VerificationCodeFormatInvalidException.class, () -> {
                        emailVerificationUseCase.executeVerifyCode(invalidCode);
                    });

                    assertEquals("Verification code format invalid", exception.getErrorCode().getMessage());
                    assertEquals(400, exception.getErrorCode().getHttpStatus());
                }
            }
        }
    }
}
