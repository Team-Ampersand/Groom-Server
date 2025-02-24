package com.ampersand.groom.domain.booking.application.port;

import com.ampersand.groom.domain.booking.domain.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingPersistencePort {
    List<Booking> findBookingByDateAndTimeAndPlace(LocalDate date, String time, String place);
}