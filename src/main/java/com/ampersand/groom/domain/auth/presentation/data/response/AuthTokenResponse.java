package com.ampersand.groom.domain.auth.presentation.data.response;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;

import java.time.LocalDateTime;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        LocalDateTime accessTokenExpiresAt,
        LocalDateTime refreshTokenExpiresAt,
        MemberRole role
) {
}