package com.ampersand.groom.domain.auth.application.usecase;

import com.ampersand.groom.domain.auth.exception.PasswordInvalidException;
import com.ampersand.groom.domain.auth.presentation.data.response.AuthTokenResponse;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import com.ampersand.groom.global.security.jwt.data.TokenDto;
import com.ampersand.groom.global.security.jwt.service.JwtIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UseCaseWithReadOnlyTransaction
@RequiredArgsConstructor
public class SignInUseCase {

    private final JwtIssueService jwtIssueService;
    private final MemberPersistencePort memberPersistencePort;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthTokenResponse execute(String username, String password) {
        Member member = memberPersistencePort.findMemberByEmail(username);
        if (passwordEncoder.matches(password, member.getPassword())) {
            TokenDto accessToken = jwtIssueService.issueAccessToken(member.getEmail(), member.getRole());
            TokenDto refreshToken = jwtIssueService.issueRefreshToken(member.getEmail());
            return new AuthTokenResponse(
                    accessToken.token(),
                    refreshToken.token(),
                    accessToken.expiration(),
                    refreshToken.expiration(),
                    member.getRole()
            );
        } else {
            throw new PasswordInvalidException();
        }
    }
}