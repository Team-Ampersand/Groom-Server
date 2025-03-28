package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.exception.RefreshTokenExpiredOrInvalidException;
import com.ampersand.groom.domain.auth.presentation.data.response.AuthTokenResponse;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import com.ampersand.groom.global.security.jwt.data.TokenDto;
import com.ampersand.groom.global.security.jwt.service.JwtIssueService;
import com.ampersand.groom.global.security.jwt.service.JwtParserService;
import com.ampersand.groom.global.security.jwt.service.JwtRefreshManagementService;
import lombok.RequiredArgsConstructor;

@UseCaseWithReadOnlyTransaction
@RequiredArgsConstructor
public class RefreshUseCase {

    private final JwtIssueService jwtIssueService;
    private final JwtParserService jwtParserService;
    private final JwtRefreshManagementService jwtRefreshManagementService;
    private final MemberPersistencePort memberPersistencePort;

    public AuthTokenResponse execute(String refreshToken) {
        if (jwtParserService.validateRefreshToken(refreshToken)) {
            String email = jwtParserService.getEmailFromRefreshToken(refreshToken);
            Member member = memberPersistencePort.findMemberByEmail(email);
            jwtRefreshManagementService.deleteRefreshToken(refreshToken);
            TokenDto newAccessToken = jwtIssueService.issueAccessToken(email, member.getRole());
            TokenDto newRefreshToken = jwtIssueService.issueRefreshToken(email);
            return new AuthTokenResponse(
                    newAccessToken.token(),
                    newRefreshToken.token(),
                    newAccessToken.expiration(),
                    newRefreshToken.expiration(),
                    member.getRole()
            );
        } else {
            throw new RefreshTokenExpiredOrInvalidException();
        }
    }
}