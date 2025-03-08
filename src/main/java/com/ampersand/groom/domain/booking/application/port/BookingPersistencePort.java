package com.ampersand.groom.domain.booking.application.port;

import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.member.domain.Member;

import java.time.LocalDate;
import java.util.List;

public interface BookingPersistencePort {
    List<Booking> findBookingByDateAndTimeAndPlace(LocalDate date, String time, String place);

    Booking findBookingByIdWithLock(Long bookingId);

    Boolean ExistsBookingByDateAndTimeAndPlace(LocalDate date, String time, String place);

    void saveBooking(Booking booking);

    void deleteBookingById(Long bookingId);
}