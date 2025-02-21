package com.ampersand.groom.domain.booking.application;

import com.ampersand.groom.domain.booking.application.port.BookingApplicationPort;
import com.ampersand.groom.domain.booking.application.usecase.FindPlaceByBookingAvailabilityUseCase;
import com.ampersand.groom.domain.booking.presentation.data.response.GetBookingResponse;
import com.ampersand.groom.domain.booking.presentation.data.response.GetPlaceResponse;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Adapter(AdapterType.INBOUND)
@RequiredArgsConstructor
public class BookingApplicationAdapter implements BookingApplicationPort {

    private final FindPlaceByBookingAvailabilityUseCase findPlaceByBookingAvailabilityUseCase;

    @Override
    public List<GetPlaceResponse> findPlaceByBookingAvailability(LocalDate date, String time, String placeType) {
        return findPlaceByBookingAvailabilityUseCase.execute(date, time, placeType);
    }

    @Override
    public List<GetBookingResponse> findBookingByDateAndTimeAndPlace(LocalDate date, String time, String place) {
        return List.of();
    }

    @Override
    public void createBooking(String time, String place, List<Long> participants) {

    }

    @Override
    public void updateBooking(Long id, String time, String place, List<Long> participants) {

    }

    @Override
    public void deleteBooking(Long id) {

    }
}
