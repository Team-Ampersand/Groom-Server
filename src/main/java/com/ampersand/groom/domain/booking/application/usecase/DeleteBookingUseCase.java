package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.exception.NotBookingPresidentException;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@UseCaseWithTransaction
@RequiredArgsConstructor
public class DeleteBookingUseCase {

    private final BookingPersistencePort bookingPersistencePort;
    private final MemberPersistencePort memberPersistencePort;

    public void execute(Long bookingId) {
        Booking booking = bookingPersistencePort.findBookingByIdWithLock(bookingId);
        if (!booking.getPresident().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())) {
            throw new NotBookingPresidentException();
        }
        bookingPersistencePort.deleteBookingById(bookingId);
    }
}