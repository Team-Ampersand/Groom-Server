package com.ampersand.groom.domain.member.presentation.data.response;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;

import java.util.List;

public record GetCurrentMemberResponse(
        Long id,
        String name,
        Integer generation,
        String email,
        Boolean isAvailable,
        MemberRole role,
        List<?> booked // TODO: booking 관련 기능 구현 시 변경
) {
}