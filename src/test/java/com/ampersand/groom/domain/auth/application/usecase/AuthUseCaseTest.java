package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.application.service.AuthService;
import com.ampersand.groom.domain.auth.domain.JwtToken;
import com.ampersand.groom.domain.auth.expection.*;
import com.ampersand.groom.domain.auth.presentation.data.Request.SignupRequest;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.persistence.repository.MemberJpaRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthUseCase 클래스의")
class AuthUseCaseTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthUseCase authUseCase;

    @DisplayName("executeSignIn 메서드는")
    @Nested
    class Describe_executeSignIn {

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
                JwtToken result = authUseCase.executeSignIn(email, password);

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
                assertThrows(PasswordInvalidException.class, () -> authUseCase.executeSignIn(email, password));
            }

            @Test
            @DisplayName("UserNotFoundException을 던진다.")
            void it_throws_user_not_found_exception() {
                // given
                String email = "unknown@example.com";
                String password = "password123";
                when(authService.signIn(email, password)).thenThrow(new UserNotFoundException());

                // when & then
                assertThrows(UserNotFoundException.class, () -> authUseCase.executeSignIn(email, password));
            }
        }
    }

    @DisplayName("executeSignup 메서드는")
    @Nested
    class Describe_executeSignup {

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
                assertDoesNotThrow(() -> authUseCase.executeSignup(signupRequest));

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
                assertThrows(UserExistException.class, () -> authUseCase.executeSignup(signupRequest));
            }
        }
    }

    @DisplayName("executeRefreshToken 메서드는")
    @Nested
    class Describe_executeRefreshToken {

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
                JwtToken result = authUseCase.executeRefreshToken(refreshToken);

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
                assertThrows(RefreshTokenExpiredOrInvalidException.class, () -> authUseCase.executeRefreshToken(refreshToken));
            }
        }

        @Nested
        @DisplayName("리프레시 토큰이 없거나 비어있는 경우")
        class Context_with_empty_refresh_token {

            @Test
            @DisplayName("ReFreshTokenRequestFormatInvalidException을 던진다.")
            void it_throws_refresh_token_request_format_invalid_exception() {
                // given
                String refreshToken = "";
                when(authService.refreshToken(refreshToken)).thenThrow(new ReFreshTokenRequestFormatInvalidException());

                // when & then
                assertThrows(ReFreshTokenRequestFormatInvalidException.class, () -> authUseCase.executeRefreshToken(refreshToken));
            }
        }
    }
}
