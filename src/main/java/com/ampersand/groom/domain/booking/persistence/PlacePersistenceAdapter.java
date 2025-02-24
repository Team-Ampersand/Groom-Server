package com.ampersand.groom.domain.booking.persistence;

import com.ampersand.groom.domain.booking.application.port.PlacePersistencePort;
import com.ampersand.groom.domain.booking.domain.Place;
import com.ampersand.groom.domain.booking.exception.PlaceNotFoundException;
import com.ampersand.groom.domain.booking.persistence.mapper.PlaceMapper;
import com.ampersand.groom.domain.booking.persistence.repository.PlaceJpaRepository;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Adapter(AdapterType.OUTBOUND)
public class PlacePersistenceAdapter implements PlacePersistencePort {

    private final PlaceJpaRepository placeJpaRepository;
    private final PlaceMapper placeMapper;

    @Override
    public List<Place> findPlaceByBookingAvailability(LocalDate date, String time, String placeType) {
        return placeJpaRepository.findPlaceByBookingAvailability(date, time, placeType).stream()
                .map(placeMapper::toDomain)
                .toList();
    }

    @Override
    public Place findPlaceByPlaceName(String placeName) {
        return placeMapper.toDomain(placeJpaRepository.findPlaceByPlaceName(placeName)
                .orElseThrow(PlaceNotFoundException::new));
    }
}