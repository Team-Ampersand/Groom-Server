package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.auth.application.port.AuthenticationPersistencePort;
import com.ampersand.groom.domain.auth.exception.PasswordInvalidException;
import com.ampersand.groom.domain.auth.exception.UserForbiddenException;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UseCaseWithTransaction
@RequiredArgsConstructor
public class UpdatePasswordUseCase {

    private final AuthenticationPersistencePort authenticationPersistencePort;
    private final MemberPersistencePort memberPersistencePort;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void execute(String email, String currentPassword, String newPassword) {
        Member member = memberPersistencePort.findMemberByEmail(email);
        if (!authenticationPersistencePort.existsAuthenticationByEmail(member.getEmail())
                || !authenticationPersistencePort.findAuthenticationByEmail(member.getEmail()).getVerified()) {
            throw new UserForbiddenException();
        }
        if (!bCryptPasswordEncoder.matches(currentPassword, member.getPassword())) {
            throw new PasswordInvalidException();
        }
        memberPersistencePort.updateMemberPassword(member.getId(), bCryptPasswordEncoder.encode(newPassword));
    }
}