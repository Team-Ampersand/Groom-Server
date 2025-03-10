package com.ampersand.groom.domain.booking.application.port;

import com.ampersand.groom.domain.booking.presentation.data.response.GetBookingResponse;
import com.ampersand.groom.domain.booking.presentation.data.response.GetPlaceResponse;

import java.time.LocalDate;
import java.util.List;

public interface BookingApplicationPort {
    List<GetPlaceResponse> findPlaceByBookingAvailability(LocalDate date, String time, String placeType);

    List<GetBookingResponse> findBookingByDateAndTimeAndPlace(LocalDate date, String time, String place);

    void createBooking(String time, String place, List<Long> participants);

    void updateBooking(Long id, String time, String place, List<Long> participants);

    void deleteBooking(Long id);

    void deleteOldBookings();
}