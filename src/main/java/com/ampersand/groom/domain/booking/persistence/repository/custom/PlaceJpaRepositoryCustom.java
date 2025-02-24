package com.ampersand.groom.domain.booking.persistence.repository.custom;

import com.ampersand.groom.domain.booking.persistence.entity.PlaceJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlaceJpaRepositoryCustom {
    List<PlaceJpaEntity> findPlaceByBookingAvailability(LocalDate date,String time,String placeType);

    Optional<PlaceJpaEntity> findPlaceByPlaceName(String placeName);
}
