package com.ampersand.groom.domain.member.persistence.repository.custom;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.persistence.entity.MemberJpaEntity;

import java.util.List;

public interface MemberRepositoryCustom {
    void updatePassword(Long id, String newPassword);

    List<MemberJpaEntity> searchMember(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role);
}