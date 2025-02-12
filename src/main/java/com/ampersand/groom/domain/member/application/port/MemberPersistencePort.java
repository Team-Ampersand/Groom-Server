package com.ampersand.groom.domain.member.application.port;

import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;

import java.util.List;

public interface MemberPersistencePort {
    List<Member> queryAllMember();

    List<Member> searchMember(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role);

    Member queryMember(Long id);

    void updateMemberPassword(Long id, String newPassword);
}