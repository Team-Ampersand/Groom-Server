package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.booking.application.port.TimeSlotPersistencePort;
import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.exception.*;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@UseCaseWithTransaction
@RequiredArgsConstructor
public class UpdateBookingUseCase {

    private final MemberPersistencePort memberPersistencePort;
    private final BookingPersistencePort bookingPersistencePort;
    private final TimeSlotPersistencePort timeSlotPersistencePort;

    public void execute(Long bookingId, String time, String place, List<Long> participants) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }
        Booking booking = bookingPersistencePort.findBookingById(bookingId);
        String email = authentication.getPrincipal().toString();
        Member president = memberPersistencePort.findMemberByEmail(email);
        if(!president.getId().equals(booking.getPresident().getId())) {
            throw new NotBookingPresidentException();
        }
        List<TimeSlot> availableTimeSlots = timeSlotPersistencePort.findAllTimeSlots();
        TimeSlot selectedTimeSlot = availableTimeSlots.stream()
                .filter(timeSlot -> timeSlot.getPlace().getPlaceName().equals(place))
                .filter(timeSlot -> timeSlot.getTimeSlotId().timeLabel().equals(time))
                .findAny()
                .orElseThrow(InvalidBookingInfomationException::new);
        if(bookingPersistencePort.ExistsBookingByDateAndTimeAndPlace(LocalDate.now(), time, place)) {
            throw new DuplicateBookingException();
        }
        if(selectedTimeSlot.getPlace().getMaxCapacity() < participants.size() + 1) {
            throw new MaxCapacityExceededException();
        }
        List<Member> participantEntities = memberPersistencePort.findMembersByIds(participants);
        if (participantEntities.size() != participants.size() ||
                participantEntities.stream().anyMatch(member -> member.getId().equals(president.getId()))) {
            throw new InvalidBookingParticipantsException();
        }
        bookingPersistencePort.saveBooking(
                Booking.builder()
                        .id(booking.getId())
                        .president(booking.getPresident())
                        .participants(participantEntities)
                        .timeSlot(selectedTimeSlot)
                        .bookingDate(booking.getBookingDate())
                        .build()
        );
    }
}
