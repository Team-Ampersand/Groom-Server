package com.ampersand.groom.domain.booking.application.port;

import com.ampersand.groom.domain.booking.domain.Place;

import java.time.LocalDate;
import java.util.List;

public interface PlacePersistencePort {
    List<Place> findPlaceByBookingAvailability(LocalDate date, String time, String placeType);
}