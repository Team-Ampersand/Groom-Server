package com.ampersand.groom.global.security.jwt.service;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.global.security.jwt.data.TokenDto;

public interface JwtIssueService {
    TokenDto issueAccessToken(String email, MemberRole roles);

    TokenDto issueRefreshToken(String email);
}