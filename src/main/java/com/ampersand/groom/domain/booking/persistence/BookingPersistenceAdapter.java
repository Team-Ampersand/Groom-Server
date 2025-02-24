package com.ampersand.groom.domain.booking.persistence;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.persistence.mapper.BookingMapper;
import com.ampersand.groom.domain.booking.persistence.repository.BookingJpaRepository;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Adapter(AdapterType.OUTBOUND)
@RequiredArgsConstructor
public class BookingPersistenceAdapter implements BookingPersistencePort {
    private final BookingJpaRepository bookingJpaRepository;
    private final BookingMapper bookingMapper;

    @Override
    public List<Booking> findBookingByDateAndTimeAndPlace(LocalDate date, String time, String place) {
        return bookingJpaRepository.findBookingByDateAndTimeAndPlace(date, time, place).stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Override
    public void saveBooking(Booking booking) {
        bookingJpaRepository.save(bookingMapper.toEntity(booking));
    }
}
