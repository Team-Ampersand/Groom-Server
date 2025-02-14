package com.ampersand.groom.auth;

import com.ampersand.groom.domain.auth.application.usecase.EmailVerificationUseCase;
import com.ampersand.groom.domain.auth.presentation.controller.AuthController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController 클래스의")
class AuthControllerTest {

    @Mock
    private EmailVerificationUseCase emailVerificationUseCase;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Nested
    @DisplayName("verifyEmail 메서드는")
    class Describe_verifyEmail {

        @Nested
        @DisplayName("유효한 인증 코드가 제공될 때")
        class Context_with_valid_code {

            @Test
            @DisplayName("인증을 성공적으로 완료한다.")
            void it_returns_verification_success() throws Exception {
                // given
                String validCode = "12345678";
                when(emailVerificationUseCase.executeVerifyEmail(validCode)).thenReturn(true);

                // when & then
                mockMvc.perform(post("/auth/email/verify-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"code\":\"" + validCode + "\"}"))
                        .andExpect(status().isResetContent())  // 205 상태 코드 반환
                        .andExpect(content().string("Verification successful."));

                // verify the interaction with the use case
                verify(emailVerificationUseCase).executeVerifyEmail(validCode);
            }
        }

        @Nested
        @DisplayName("인증 코드의 요청 형식이 올바르지 않을 때")
        class Context_with_invalid_format_code {

            @Test
            @DisplayName("400 Bad Request 응답을 반환한다.")
            void it_returns_bad_request_for_invalid_format() throws Exception {
                // given
                String invalidCode = "12345";  // 5자리 코드 (잘못된 형식)

                // when & then
                mockMvc.perform(post("/auth/email/verify-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"code\":\"" + invalidCode + "\"}"))
                        .andExpect(status().isBadRequest())  // 400 상태 코드 반환
                        .andExpect(content().string("Invalid verification code format."));

                // verify that the use case is not called in this case
                verify(emailVerificationUseCase, never()).executeVerifyEmail(any());
            }
        }

        @Nested
        @DisplayName("유효한 인증 코드가 제공되었으나 인증에 실패할 때")
        class Context_with_failed_verification {

            @Test
            @DisplayName("401 Unauthorized 응답을 반환한다.")
            void it_returns_invalid_or_expired_code_message() throws Exception {
                // given
                String invalidCode = "00000000";  // 유효하지 않은 인증 코드
                when(emailVerificationUseCase.executeVerifyEmail(invalidCode)).thenReturn(false);

                // when & then
                mockMvc.perform(post("/auth/email/verify-email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"code\":\"" + invalidCode + "\"}"))
                        .andExpect(status().isUnauthorized())  // 401 상태 코드 반환
                        .andExpect(content().string("Invalid or expired verification code"));

                // verify the interaction with the use case
                verify(emailVerificationUseCase).executeVerifyEmail(invalidCode);
            }
        }
    }

    @Nested
    @DisplayName("signup 메서드는")
    class Describe_signup {

        @Nested
        @DisplayName("유효한 이메일이 제공될 때")
        class Context_with_valid_email {

            @Test
            @DisplayName("인증 이메일을 전송한다.")
            void it_returns_verification_email_sent() throws Exception {
                // given
                String validEmail = "test@example.com";

                // when & then
                mockMvc.perform(post("/auth/email/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"" + validEmail + "\"}"))
                        .andExpect(status().isResetContent())
                        .andExpect(content().string("Verification email sent"));

                // verify the interaction with the use case
                verify(emailVerificationUseCase).executeSendSignupVerificationEmail(validEmail);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이메일이 제공될 때")
        class Context_with_invalid_email {

            @Test
            @DisplayName("잘못된 이메일 형식 메시지를 반환한다.")
            void it_returns_invalid_email_format_message() throws Exception {
                // given
                String invalidEmail = "tooooolongemail@example.com";

                // when & then
                mockMvc.perform(post("/auth/email/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"" + invalidEmail + "\"}"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Invalid email format."));
            }
        }
    }

    @Nested
    @DisplayName("refresh 메서드는")
    class Describe_refresh {

        @Nested
        @DisplayName("유효한 이메일이 제공될 때")
        class Context_with_valid_email {

            @Test
            @DisplayName("인증 이메일을 전송한다.")
            void it_returns_verification_email_sent() throws Exception {
                // given
                String validEmail = "test@example.com";

                // when & then
                mockMvc.perform(post("/auth/email/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"" + validEmail + "\"}"))
                        .andExpect(status().isResetContent())
                        .andExpect(content().string("Verification email sent"));

                // verify the interaction with the use case
                verify(emailVerificationUseCase).executeSendPasswordResetEmail(validEmail);
            }
        }

        @Nested
        @DisplayName("유효하지 않은 이메일이 제공될 때")
        class Context_with_invalid_email {

            @Test
            @DisplayName("잘못된 이메일 형식 메시지를 반환한다.")
            void it_returns_invalid_email_format_message() throws Exception {
                // given
                String invalidEmail = "tooooolongemail@example.com";

                // when & then
                mockMvc.perform(post("/auth/email/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"" + invalidEmail + "\"}"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Invalid email format."));
            }
        }
    }
}
