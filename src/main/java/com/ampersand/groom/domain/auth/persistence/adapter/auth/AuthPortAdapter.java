package com.ampersand.groom.domain.auth.persistence.adapter.auth;

import com.ampersand.groom.domain.auth.application.port.AuthPort;
import com.ampersand.groom.domain.member.persistence.entity.MemberJpaEntity;
import com.ampersand.groom.domain.member.persistence.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthPortAdapter implements AuthPort {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<MemberJpaEntity> findByEmail(String email) {
        return memberJpaRepository.findAll().stream()
                .filter(member -> member.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void save(MemberJpaEntity member) {
        memberJpaRepository.save(member);
    }
}
