package com.ampersand.groom.domain.member.presentation.data.response;

import java.time.LocalDate;
import java.util.List;

public record GetMemberBookingResponse(
        String place,
        List<GetMemberResponse> participants,
        LocalDate date,
        String time
) {
}