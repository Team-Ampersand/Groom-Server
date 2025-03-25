package com.ampersand.groom.domain.auth.application;

import com.ampersand.groom.domain.auth.application.service.AuthService;
import com.ampersand.groom.domain.auth.domain.JwtToken;
import com.ampersand.groom.domain.auth.exception.*;
import com.ampersand.groom.domain.auth.presentation.data.request.SignupRequest;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 클래스의")
class AuthServiceTest {

    @Mock
    private AuthService authService;

    @DisplayName("signIn 메서드는")
    @Nested
    class Describe_signIn {

        @Nested
        @DisplayName("정상적인 로그인 시")
        class Context_with_valid_credentials {

            @Test
            @DisplayName("JWT 토큰을 반환한다.")
            void it_returns_jwt_token() {
                // given
                String email = "test@example.com";
                String password = "password123";
                JwtToken validToken = JwtToken.builder()
                        .accessToken("validAccessToken")
                        .refreshToken("validRefreshToken")
                        .accessTokenExpiration(Instant.now().plusMillis(1000))
                        .refreshTokenExpiration(Instant.now().plusMillis(1000))
                        .role(MemberRole.ROLE_STUDENT)
                        .build();
                when(authService.signIn(email, password)).thenReturn(validToken);

                // when
                JwtToken result = authService.signIn(email, password);

                // then
                verify(authService).signIn(email, password);
                assertNotNull(result);
                assertEquals("validAccessToken", result.getAccessToken());
            }
        }

        @Nested
        @DisplayName("잘못된 로그인 시도 시")
        class Context_with_invalid_credentials {

            @Test
            @DisplayName("PasswordInvalidException을 던진다.")
            void it_throws_password_invalid_exception() {
                // given
                String email = "test@example.com";
                String password = "wrongPassword";
                when(authService.signIn(email, password)).thenThrow(new PasswordInvalidException());

                // when & then
                assertThrows(PasswordInvalidException.class, () -> authService.signIn(email, password));
            }

            @Test
            @DisplayName("UserNotFoundException을 던진다.")
            void it_throws_user_not_found_exception() {
                // given
                String email = "unknown@example.com";
                String password = "password123";
                when(authService.signIn(email, password)).thenThrow(new UserNotFoundException());

                // when & then
                assertThrows(UserNotFoundException.class, () -> authService.signIn(email, password));
            }
        }
    }

    @DisplayName("signup 메서드는")
    @Nested
    class Describe_signup {

        @Nested
        @DisplayName("정상적인 회원가입 시")
        class Context_with_valid_signup {

            @Test
            @DisplayName("회원가입을 성공적으로 수행한다.")
            void it_signs_up_successfully() {
                // given
                SignupRequest signupRequest = new SignupRequest("test@example.com", "password123", "John Doe");
                doNothing().when(authService).signup(signupRequest);

                // when
                assertDoesNotThrow(() -> authService.signup(signupRequest));

                // then
                verify(authService).signup(signupRequest);
            }
        }

        @Nested
        @DisplayName("중복된 이메일로 회원가입 시")
        class Context_with_existing_email {

            @Test
            @DisplayName("UserExistException을 던진다.")
            void it_throws_user_exist_exception() {
                // given
                SignupRequest signupRequest = new SignupRequest("test@example.com", "password123", "John Doe");
                doThrow(new UserExistException()).when(authService).signup(signupRequest);

                // when & then
                assertThrows(UserExistException.class, () -> authService.signup(signupRequest));
            }
        }
    }

    @DisplayName("refreshToken 메서드는")
    @Nested
    class Describe_refreshToken {

        @Nested
        @DisplayName("정상적인 리프레시 토큰")
        class Context_with_valid_refresh_token {

            @Test
            @DisplayName("새로운 JWT 토큰을 반환한다.")
            void it_returns_new_jwt_token() {
                // given
                String refreshToken = "validRefreshToken";
                JwtToken validToken = JwtToken.builder()
                        .accessToken("newAccessToken")
                        .refreshToken("newRefreshToken")
                        .accessTokenExpiration(Instant.now().plusMillis(1000))
                        .refreshTokenExpiration(Instant.now().plusMillis(1000))
                        .role(MemberRole.ROLE_STUDENT)
                        .build();
                when(authService.refreshToken(refreshToken)).thenReturn(validToken);

                // when
                JwtToken result = authService.refreshToken(refreshToken);

                // then
                verify(authService).refreshToken(refreshToken);
                assertNotNull(result);
                assertEquals("newAccessToken", result.getAccessToken());
            }
        }

        @Nested
        @DisplayName("잘못된 리프레시 토큰")
        class Context_with_invalid_refresh_token {

            @Test
            @DisplayName("RefreshTokenExpiredOrInvalidException을 던진다.")
            void it_throws_refresh_token_expired_or_invalid_exception() {
                // given
                String refreshToken = "invalidRefreshToken";
                when(authService.refreshToken(refreshToken)).thenThrow(new RefreshTokenExpiredOrInvalidException());

                // when & then
                assertThrows(RefreshTokenExpiredOrInvalidException.class, () -> authService.refreshToken(refreshToken));
            }
        }

        @Nested
        @DisplayName("리프레시 토큰이 없거나 비어있는 경우")
        class Context_with_empty_refresh_token {

            @Test
            @DisplayName("RefreshTokenRequestFormatInvalidException을 던진다.")
            void it_throws_refresh_token_request_format_invalid_exception() {
                // given
                String refreshToken = "";
                when(authService.refreshToken(refreshToken)).thenThrow(new RefreshTokenRequestFormatInvalidException());

                // when & then
                assertThrows(RefreshTokenRequestFormatInvalidException.class, () -> authService.refreshToken(refreshToken));
            }
        }
    }
}
