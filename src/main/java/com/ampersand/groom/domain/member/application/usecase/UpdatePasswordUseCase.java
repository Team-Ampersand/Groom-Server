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

    public void execute(Long id, String currentPassword, String newPassword) {
        Member member = memberPersistencePort.findMemberById(id);
        if (!authenticationPersistencePort.existsAuthenticationByEmail(member.getEmail())) {
            throw new UserForbiddenException();
        }
        if (!authenticationPersistencePort.findAuthenticationByEmail(member.getEmail()).getVerified()) {
            throw new UserForbiddenException();
        }
        if(!bCryptPasswordEncoder.matches(member.getPassword(), currentPassword)){
            throw new PasswordInvalidException();
        }
        memberPersistencePort.updateMemberPassword(
                member.getId(),
                bCryptPasswordEncoder.encode(newPassword)
        );
    }
}