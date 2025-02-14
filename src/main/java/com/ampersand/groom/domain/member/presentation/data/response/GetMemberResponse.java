package com.ampersand.groom.domain.member.presentation.data.response;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;

public record GetMemberResponse(
        Long id,
        String name,
        Integer generation,
        String email,
        Boolean isAvailable,
        MemberRole role
) {
}