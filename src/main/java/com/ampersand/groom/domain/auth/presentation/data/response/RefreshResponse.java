package com.ampersand.groom.domain.auth.presentation.data.response;

import java.time.LocalDateTime;

import com.ampersand.groom.domain.member.domain.Member;
import lombok.Getter;

@Getter
public class RefreshResponse {

    private final String accessToken;
    private final String refreshToken;
    private final LocalDateTime accessTokenExpiredAt;
    private final LocalDateTime refreshTokenExpiredAt;
    private final Member role;

    public RefreshResponse(String accessToken, String refreshToken, LocalDateTime accessTokenExpiredAt, LocalDateTime refreshTokenExpiredAt, Member role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
        this.role = role;
    }
}
