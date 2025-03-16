package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UseCaseWithTransaction
@RequiredArgsConstructor
public class UpdatePasswordUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void execute(Long id, String currentPassword, String newPassword) {
        bCryptPasswordEncoder.matches(memberPersistencePort.findMemberById(id).getPassword(), currentPassword);
    }

}
