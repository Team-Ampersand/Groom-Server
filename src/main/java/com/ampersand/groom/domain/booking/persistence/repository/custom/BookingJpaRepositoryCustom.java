package com.ampersand.groom.domain.booking.persistence.repository.custom;

import com.ampersand.groom.domain.booking.persistence.entity.BookingJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingJpaRepositoryCustom {
    List<BookingJpaEntity> findBookingByDateAndTimeAndPlace(LocalDate date, String time, String placeType);

    Optional<BookingJpaEntity> findByIdWithLock(Long bookingId);

    Boolean ExistsBookingByDateAndTimeAndPlace(LocalDate date, String time, String placeType);
}