package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.booking.application.port.PlacePersistencePort;
import com.ampersand.groom.domain.booking.application.port.TimeSlotPersistencePort;
import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.domain.Place;
import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.exception.DuplicateBookingException;
import com.ampersand.groom.domain.booking.exception.InvalidBookingParticipantsException;
import com.ampersand.groom.domain.booking.exception.InvalidBookingTimeException;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithTransaction;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@UseCaseWithTransaction
@RequiredArgsConstructor
public class CreateBookingUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final BookingPersistencePort bookingPersistencePort;
    private final PlacePersistencePort placePersistencePort;
    private final TimeSlotPersistencePort timeSlotPersistencePort;

    public void execute(String time, String place, List<Long> participants) {
        Place placeEntity = placePersistencePort.findPlaceByPlaceName(place);
        List<TimeSlot> availableTimeSlots = timeSlotPersistencePort.findTimeSlotByPlace(placeEntity.getPlaceName());
        TimeSlot selectedTimeSlot = availableTimeSlots.stream()
                .filter(timeSlot -> timeSlot.getTimeSlotId().timeLabel().equals(time))
                .findAny()
                .orElseThrow(InvalidBookingTimeException::new);
        bookingPersistencePort.findBookingByDateAndTimeAndPlace(LocalDate.now(), selectedTimeSlot.getTimeSlotId().timeLabel(), place)
                .stream()
                .findAny()
                .ifPresent(booking -> {
                    throw new DuplicateBookingException();
                });
        // TODO: Spring Security/JWT에서 현재 사용자 정보 가져오도록 수정하기
/*        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        Member president = (Member) authentication.getPrincipal(); */

        List<Member> participantEntities = memberPersistencePort.findMembersByIds(participants);
        Member president = memberPersistencePort.findMemberById(2L);
        if (participantEntities.size() != participants.size() ||
                participantEntities.stream().anyMatch(member -> member.getId().equals(president.getId()))) {
            throw new InvalidBookingParticipantsException();
        }
        Booking newBooking = Booking.builder()
                .bookingDate(LocalDate.now())
                .timeSlot(selectedTimeSlot)
                .president(president)
                .participants(participantEntities)
                .build();
        bookingPersistencePort.saveBooking(newBooking);
    }
}