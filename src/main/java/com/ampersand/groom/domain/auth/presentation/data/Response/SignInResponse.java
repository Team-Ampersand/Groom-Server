package com.ampersand.groom.domain.auth.presentation.data.Response;

import com.ampersand.groom.domain.member.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignInResponse {

    private final String accessToken;
    private final String refreshToken;
    private final LocalDateTime accessTokenExpiresAt;
    private final LocalDateTime refreshTokenExpiresAt;
    private final Member role;


    public SignInResponse(String accessToken, String refreshToken, LocalDateTime accessTokenExpiresAt, LocalDateTime refreshTokenExpiresAt, Member role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
        this.role = role;
    }
}
