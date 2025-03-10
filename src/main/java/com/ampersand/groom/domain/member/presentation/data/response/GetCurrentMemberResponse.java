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
        List<GetMemberBookingResponse> booked
) {
}