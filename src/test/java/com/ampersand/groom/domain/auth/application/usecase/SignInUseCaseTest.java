package com.ampersand.groom.domain.auth.application.usecase;


import com.ampersand.groom.domain.auth.exception.PasswordInvalidException;
import com.ampersand.groom.domain.auth.presentation.data.response.AuthTokenResponse;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.global.security.jwt.data.TokenDto;
import com.ampersand.groom.global.security.jwt.service.JwtIssueService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("로그인 UseCase 클래스의")
class SignInUseCaseTest {

    @Mock
    private JwtIssueService jwtIssueService;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private SignInUseCase signInUseCase;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("비밀번호가 일치하면")
        class Context_with_valid_password {

            @Test
            @DisplayName("AccessToken과 RefreshToken을 반환한다.")
            void it_returns_tokens() {
                // Given
                String email = "s00001@example.com";
                String password = "password123";
                String encodedPassword = "encodedPassword";
                MemberRole role = MemberRole.ROLE_STUDENT;
                Member member = Member.builder()
                        .email(email)
                        .password(encodedPassword)
                        .role(role)
                        .build();
                TokenDto accessToken = new TokenDto("access-token", LocalDateTime.now().plusMinutes(30));
                TokenDto refreshToken = new TokenDto("refresh-token", LocalDateTime.now().plusDays(7));
                when(memberPersistencePort.findMemberByEmail(email)).thenReturn(member);
                when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
                when(jwtIssueService.issueAccessToken(email, role)).thenReturn(accessToken);
                when(jwtIssueService.issueRefreshToken(email)).thenReturn(refreshToken);

                // When
                AuthTokenResponse response = signInUseCase.execute(email, password);

                // Then
                assertAll(
                        () -> assertEquals(accessToken.token(), response.accessToken()),
                        () -> assertEquals(refreshToken.token(), response.refreshToken()),
                        () -> assertEquals(accessToken.expiration(), response.accessTokenExpiresAt()),
                        () -> assertEquals(refreshToken.expiration(), response.refreshTokenExpiresAt()),
                        () -> assertEquals(role, response.role())
                );
            }
        }

        @Nested
        @DisplayName("비밀번호가 일치하지 않으면")
        class Context_with_invalid_password {

            @Test
            @DisplayName("PasswordInvalidException을 던진다.")
            void it_throws_PasswordInvalidException() {
                // Given
                String email = "s00001@example.com";
                String password = "wrongPassword";
                String encodedPassword = "encodedPassword";
                Member member = Member.builder()
                        .email(email)
                        .password(encodedPassword)
                        .role(MemberRole.ROLE_STUDENT)
                        .build();
                when(memberPersistencePort.findMemberByEmail(email)).thenReturn(member);
                when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

                // When & Then
                assertThrows(PasswordInvalidException.class, () ->
                        signInUseCase.execute(email, password)
                );
            }
        }
    }
}