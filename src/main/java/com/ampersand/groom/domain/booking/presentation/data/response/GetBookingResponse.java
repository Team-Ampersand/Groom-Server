package com.ampersand.groom.domain.booking.presentation.data.response;

import com.ampersand.groom.domain.member.domain.Member;

import java.time.LocalDate;
import java.util.List;

public record GetBookingResponse(
        String place,
        List<Member> participants,
        LocalDate date,
        String time
) {
}