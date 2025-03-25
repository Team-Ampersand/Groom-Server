package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithTransaction;
import lombok.RequiredArgsConstructor;

@UseCaseWithTransaction
@RequiredArgsConstructor
public class DeleteOldBookingUseCase {

    private final BookingPersistencePort bookingPersistencePort;

    public void execute() {
        bookingPersistencePort.deleteOldBookings();
    }
}