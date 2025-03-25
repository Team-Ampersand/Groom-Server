package com.ampersand.groom.domain.booking.presentation.data.response;

import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;

import java.time.LocalDate;
import java.util.List;

public record GetBookingResponse(
        Long id,
        String place,
        GetMemberResponse president,
        List<GetMemberResponse> participants,
        LocalDate date,
        String time
) {
}