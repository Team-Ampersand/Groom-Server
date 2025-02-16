package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.application.service.EmailVerificationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailVerificationUseCase 클래스의")
class EmailVerificationUseCaseTest {

    @Mock
    private EmailVerificationService emailVerificationService;

    @InjectMocks
    private EmailVerificationUseCase emailVerificationUseCase;

    @Nested
    @DisplayName("executeSendSignupVerificationEmail 메서드는")
    class Describe_executeSendSignupVerificationEmail {

        @Nested
        @DisplayName("유효한 이메일이 제공될 때")
        class Context_with_valid_email {

            @Test
            @DisplayName("회원가입 인증 이메일을 전송한다.")
            void it_sends_signup_verification_email() {
                // given
                String email = "test@example.com";

                // when
                emailVerificationUseCase.executeSendSignupVerificationEmail(email);

                // then
                verify(emailVerificationService).sendSignupVerificationEmail(email);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이메일이 제공될 때")
        class Context_with_invalid_email {

            @Test
            @DisplayName("예외를 발생시킨다.")
            void it_throws_exception() {
                // given
                String email = "invalid-email";
                doThrow(new IllegalArgumentException("Invalid email"))
                        .when(emailVerificationService).sendSignupVerificationEmail(email);

                // when & then
                assertThrows(IllegalArgumentException.class,
                        () -> emailVerificationUseCase.executeSendSignupVerificationEmail(email));
            }
        }
    }

    @Nested
    @DisplayName("executeSendPasswordResetEmail 메서드는")
    class Describe_executeSendPasswordResetEmail {

        @Nested
        @DisplayName("유효한 이메일이 제공될 때")
        class Context_with_valid_email {

            @Test
            @DisplayName("비밀번호 변경 인증 이메일을 전송한다.")
            void it_sends_password_reset_email() {
                // given
                String email = "test@example.com";

                // when
                emailVerificationUseCase.executeSendPasswordResetEmail(email);

                // then
                verify(emailVerificationService).sendPasswordResetEmail(email);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이메일이 제공될 때")
        class Context_with_invalid_email {

            @Test
            @DisplayName("예외를 발생시킨다.")
            void it_throws_exception() {
                // given
                String email = "invalid-email";
                doThrow(new IllegalArgumentException("Invalid email"))
                        .when(emailVerificationService).sendPasswordResetEmail(email);

                // when & then
                assertThrows(IllegalArgumentException.class,
                        () -> emailVerificationUseCase.executeSendPasswordResetEmail(email));
            }
        }
    }

    @Nested
    @DisplayName("executeVerifyCode 메서드는")
    class Describe_executeVerifyCode {

        @Nested
        @DisplayName("유효한 인증 코드가 제공될 때")
        class Context_with_valid_code {

            @Test
            @DisplayName("인증 코드를 검증한다.")
            void it_verifies_the_code() {
                // given
                String code = "123456";

                // when
                emailVerificationUseCase.executeVerifyCode(code);

                // then
                verify(emailVerificationService).verifyCode(code);
            }
        }

        @Nested
        @DisplayName("잘못된 형식의 인증 코드가 제공될 때")
        class Context_with_invalid_code_form {

            @Test
            @DisplayName("예외를 발생시킨다.")
            void it_throws_exception() {
                // given
                String code = "1234";
                doThrow(new IllegalArgumentException("Invalid code"))
                        .when(emailVerificationService).verifyCode(code);

                //when & then
                assertThrows(IllegalArgumentException.class, () -> emailVerificationUseCase.executeVerifyCode(code));

            }
        }

        @Nested
        @DisplayName("유효하지 않은 인증 코드가 제공될 때")
        class Context_with_invalid_code {

            @Test
            @DisplayName("예외를 발생시킨다.")
            void it_throws_exception() {
                // given
                String code = "invalid-code";
                doThrow(new IllegalArgumentException("Invalid verification code"))
                        .when(emailVerificationService).verifyCode(code);

                // when & then
                assertThrows(IllegalArgumentException.class,
                        () -> emailVerificationUseCase.executeVerifyCode(code));
            }
        }
    }

    @Nested
    @DisplayName("executeVerifyEmail 메서드는")
    class Describe_executeVerifyEmail {

        @Nested
        @DisplayName("유효한 이메일이 제공될 때")
        class Context_with_valid_email {

            @Test
            @DisplayName("이메일을 검증한다.")
            void it_verifies_the_email() {
                // given
                String email = "test@example.com";

                // when
                emailVerificationUseCase.executeVerifyEmail(email);

                // then
                verify(emailVerificationService).verifyEmail(email);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이메일이 제공될 때")
        class Context_with_invalid_email {

            @Test
            @DisplayName("예외를 발생시킨다.")
            void it_throws_exception() {
                // given
                String email = "invalid-email";
                doThrow(new IllegalArgumentException("Invalid email"))
                        .when(emailVerificationService).verifyEmail(email);

                // when & then
                assertThrows(IllegalArgumentException.class,
                        () -> emailVerificationUseCase.executeVerifyEmail(email));
            }
        }
    }
}
