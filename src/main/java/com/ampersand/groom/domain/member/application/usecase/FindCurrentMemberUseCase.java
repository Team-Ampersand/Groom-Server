package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.presentation.data.response.GetCurrentMemberResponse;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberBookingResponse;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@UseCaseWithReadOnlyTransaction
@RequiredArgsConstructor
public class FindCurrentMemberUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final BookingPersistencePort bookingPersistencePort;
    private final MemberMapper memberMapper;

    public GetCurrentMemberResponse execute() {
        Member currentMember = memberPersistencePort.findMemberByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<GetMemberBookingResponse> bookings = bookingPersistencePort.findBookingByMemberId(currentMember.getId()).stream().map(
                booking -> new GetMemberBookingResponse(
                        booking.getTimeSlot().getPlace().getPlaceName(),
                        booking.getParticipants().stream().map(memberMapper::toResponse).toList(),
                        booking.getBookingDate(),
                        booking.getTimeSlot().getTimeSlotId().timeLabel()
                )
        ).toList();
        return new GetCurrentMemberResponse(
                currentMember.getId(),
                currentMember.getName(),
                currentMember.getGeneration(),
                currentMember.getEmail(),
                currentMember.getIsAvailable(),
                currentMember.getRole(),
                bookings
        );
    }
}