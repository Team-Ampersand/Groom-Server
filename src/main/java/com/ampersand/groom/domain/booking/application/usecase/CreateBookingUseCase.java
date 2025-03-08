package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.booking.application.port.TimeSlotPersistencePort;
import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.exception.DuplicateBookingException;
import com.ampersand.groom.domain.booking.exception.InvalidBookingParticipantsException;
import com.ampersand.groom.domain.booking.exception.InvalidBookingInfomationException;
import com.ampersand.groom.domain.booking.exception.MaxCapacityExceededException;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.*;

@UseCaseWithTransaction
@RequiredArgsConstructor
public class CreateBookingUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final BookingPersistencePort bookingPersistencePort;
    private final TimeSlotPersistencePort timeSlotPersistencePort;

    public void execute(String time, String place, List<Long> participants) {
        List<TimeSlot> availableTimeSlots = timeSlotPersistencePort.findAllTimeSlots();
        TimeSlot selectedTimeSlot = availableTimeSlots.stream()
                .filter(timeSlot -> timeSlot.getPlace().getPlaceName().equals(place))
                .filter(timeSlot -> timeSlot.getTimeSlotId().timeLabel().equals(time))
                .findAny()
                .orElseThrow(InvalidBookingInfomationException::new);
        if(selectedTimeSlot.getPlace().getMaxCapacity() < participants.size() + 1) {
            throw new MaxCapacityExceededException();
        }
        bookingPersistencePort.findBookingByDateAndTimeAndPlace(LocalDate.now(), selectedTimeSlot.getTimeSlotId().timeLabel(), place)
                .stream()
                .findAny()
                .ifPresent(booking -> {
                    throw new DuplicateBookingException();
                });
        List<Member> participantEntities = memberPersistencePort.findMembersByIds(participants);
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member president = memberPersistencePort.findMemberByEmail(email);
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