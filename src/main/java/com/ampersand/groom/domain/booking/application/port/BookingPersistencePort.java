package com.ampersand.groom.domain.booking.application.port;

import com.ampersand.groom.domain.booking.domain.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingPersistencePort {
    List<Booking> findBookingByDateAndTimeAndPlace(LocalDate date, String time, String place);

    List<Booking> findBookingByDateAndTimeAndPlaceWithLock(LocalDate date, String time, String place);

    List<Booking> findBookingByMemberId(Long memberId);

    Booking findBookingByIdWithLock(Long bookingId);

    Boolean existsBookingByDateAndTimeAndPlace(LocalDate date, String time, String place);

    void saveBooking(Booking booking);

    void deleteBookingById(Long bookingId);
}