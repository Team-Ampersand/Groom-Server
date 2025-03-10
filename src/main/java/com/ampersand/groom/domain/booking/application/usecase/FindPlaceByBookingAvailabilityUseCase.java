package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.PlacePersistencePort;
import com.ampersand.groom.domain.booking.persistence.mapper.PlaceMapper;
import com.ampersand.groom.domain.booking.presentation.data.response.GetPlaceResponse;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@UseCaseWithReadOnlyTransaction
@RequiredArgsConstructor
public class FindPlaceByBookingAvailabilityUseCase {

    private final PlacePersistencePort placePersistencePort;
    private final PlaceMapper placeMapper;

    public List<GetPlaceResponse> execute(LocalDate date, String time, String placeType) {
        return placePersistencePort.findPlaceByBookingAvailability(date, time, placeType).stream()
                .map(placeMapper::toResponse)
                .toList();
    }
}
