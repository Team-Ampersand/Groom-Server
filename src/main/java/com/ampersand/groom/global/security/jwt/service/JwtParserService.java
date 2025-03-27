package com.ampersand.groom.global.security.jwt.service;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtParserService {
    String getEmailFromAccessToken(String token);

    MemberRole getRolesFromAccessToken(String token);

    String getEmailFromRefreshToken(String token);

    Boolean validateAccessToken(String token);

    Boolean validateRefreshToken(String token);

    String resolveToken(HttpServletRequest request);
}