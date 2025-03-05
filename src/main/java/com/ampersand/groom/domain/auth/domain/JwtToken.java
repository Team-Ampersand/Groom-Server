package com.ampersand.groom.domain.auth.domain;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class JwtToken {

    private String accessToken;
    private String refreshToken;
    private Instant accessTokenExpiration;
    private Instant refreshTokenExpiration;
    private MemberRole role;
}
