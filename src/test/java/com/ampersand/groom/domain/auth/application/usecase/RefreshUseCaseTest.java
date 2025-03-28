package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.exception.RefreshTokenExpiredOrInvalidException;
import com.ampersand.groom.domain.auth.presentation.data.response.AuthTokenResponse;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.global.security.jwt.data.TokenDto;
import com.ampersand.groom.global.security.jwt.service.JwtIssueService;
import com.ampersand.groom.global.security.jwt.service.JwtParserService;
import com.ampersand.groom.global.security.jwt.service.JwtRefreshManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JWT 재발급 UseCase 클래스의")
class RefreshUseCaseTest {

    @Mock
    private JwtIssueService jwtIssueService;

    @Mock
    private JwtParserService jwtParserService;

    @Mock
    private JwtRefreshManagementService jwtRefreshManagementService;

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @InjectMocks
    private RefreshUseCase refreshUseCase;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 리프레시 토큰일 때")
        class Context_with_valid_refresh_token {

            @Test
            @DisplayName("새로운 액세스 토큰과 리프레시 토큰을 반환한다.")
            void it_returns_new_tokens() {
                String refreshToken = "valid-refresh-token";
                String email = "s00001@gsm.hs.kr";
                MemberRole role = MemberRole.ROLE_STUDENT;
                Member member = Member.builder().email(email).role(role).build();
                LocalDateTime accessTokenExpiration = LocalDateTime.now().plusMinutes(30);
                LocalDateTime refreshTokenExpiration = LocalDateTime.now().plusDays(7);
                TokenDto newAccessToken = new TokenDto("new-access-token", accessTokenExpiration);
                TokenDto newRefreshToken = new TokenDto("new-refresh-token", refreshTokenExpiration);
                when(jwtParserService.validateRefreshToken(refreshToken)).thenReturn(true);
                when(jwtParserService.getEmailFromRefreshToken(refreshToken)).thenReturn(email);
                when(memberPersistencePort.findMemberByEmail(email)).thenReturn(member);
                when(jwtIssueService.issueAccessToken(email, role)).thenReturn(newAccessToken);
                when(jwtIssueService.issueRefreshToken(email)).thenReturn(newRefreshToken);

                // When
                AuthTokenResponse response = refreshUseCase.execute(refreshToken);

                // Then
                assertAll(
                        () -> assertEquals("new-access-token", response.accessToken()),
                        () -> assertEquals("new-refresh-token", response.refreshToken()),
                        () -> assertEquals(accessTokenExpiration, response.accessTokenExpiresAt()),
                        () -> assertEquals(refreshTokenExpiration, response.refreshTokenExpiresAt()),
                        () -> assertEquals(role, response.role())
                );
                verify(jwtRefreshManagementService).deleteRefreshToken(refreshToken);
            }

            @Nested
            @DisplayName("유효하지 않은 리프레시 토큰일 때")
            class Context_with_invalid_refresh_token {

                @Test
                @DisplayName("RefreshTokenExpiredOrInvalidException을 던진다.")
                void it_throws_RefreshTokenExpiredOrInvalidException() {
                    // Given
                    String invalidToken = "expired-token";
                    when(jwtParserService.validateRefreshToken(invalidToken)).thenReturn(false);

                    // When & Then
                    assertThrows(RefreshTokenExpiredOrInvalidException.class, () ->
                            refreshUseCase.execute(invalidToken)
                    );
                    verify(jwtRefreshManagementService, never()).deleteRefreshToken(any());
                }
            }
        }
    }
}