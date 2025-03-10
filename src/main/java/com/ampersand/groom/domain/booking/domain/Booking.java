package com.ampersand.groom.domain.booking.domain;

import com.ampersand.groom.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class Booking {
    private final Long id;
    private final Member president;
    private final List<Member> participants;
    private final TimeSlot timeSlot;
    private final LocalDate bookingDate;
}