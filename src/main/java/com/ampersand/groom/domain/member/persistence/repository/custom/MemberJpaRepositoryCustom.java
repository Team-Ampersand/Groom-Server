package com.ampersand.groom.domain.member.persistence.repository.custom;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.persistence.entity.MemberJpaEntity;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepositoryCustom {
    List<MemberJpaEntity> findMembersByCriteria(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role);

    List<MemberJpaEntity> findMembersByIds(List<Long> ids);

    Optional<MemberJpaEntity> findMemberByEmail(String email);

    void updatePassword(Long id, String newPassword);
}