package com.ampersand.groom.domain.member.application.port;

public interface MemberPersistenceWritePort {
    void updateMemberPassword(Long id, String newPassword);
}