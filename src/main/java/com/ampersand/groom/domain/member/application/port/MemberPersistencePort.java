package com.ampersand.groom.domain.member.application.port;

import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;

import java.util.List;

public interface MemberPersistencePort {
    List<Member> findAllMembers();

    List<Member> findMembersByCriteria(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role);

    Member findMemberById(Long id);

    Member findMemberByEmail(String email);

    List<Member> findMembersByIds(List<Long> ids);

    void updateMemberPassword(Long id, String newPassword);
}