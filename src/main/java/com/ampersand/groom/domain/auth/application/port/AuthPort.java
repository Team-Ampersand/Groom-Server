package com.ampersand.groom.domain.auth.application.port;

import com.ampersand.groom.domain.member.persistence.entity.MemberJpaEntity;

import java.util.Optional;

public interface AuthPort {

    Optional<MemberJpaEntity> findByEmail(String email);

    void save(MemberJpaEntity member);
}
