package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.persistence.BookingPersistenceAdapter;
import com.ampersand.groom.domain.booking.persistence.mapper.BookingMapper;
import com.ampersand.groom.domain.booking.presentation.data.response.GetBookingResponse;
import com.ampersand.groom.global.annotation.usecase.UseCaseWithReadOnlyTransaction;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@UseCaseWithReadOnlyTransaction
public class FindBookingByDateAndTimeAndPlaceUseCase {

    private final BookingPersistenceAdapter bookingPersistenceAdapter;
    private final BookingMapper bookingMapper;

    public List<GetBookingResponse> execute(LocalDate date, String time, String place) {
        return bookingPersistenceAdapter.findBookingByDateAndTimeAndPlace(date, time, place).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }
}
