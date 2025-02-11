package com.ampersand.groom.domain.member.application.port;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.presentation.data.response.GetCurrentMemberResponse;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;

import java.util.List;

public interface MemberApplicationPort {
    List<GetMemberResponse> getAllMembers();

    List<GetMemberResponse> searchMembers(Long id, String name, Integer generation, String email, Boolean isAvailable, MemberRole role);

    GetCurrentMemberResponse getCurrentMember();

    void updatePassword(Long id, String currentPassword, String newPassword);
}