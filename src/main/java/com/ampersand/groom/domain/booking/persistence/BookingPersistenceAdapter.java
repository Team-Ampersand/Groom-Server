package com.ampersand.groom.domain.booking.persistence;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.exception.BookingNotFoundException;
import com.ampersand.groom.domain.booking.persistence.mapper.BookingMapper;
import com.ampersand.groom.domain.booking.persistence.repository.BookingJpaRepository;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ampersand.groom.domain.booking.persistence.entity.QBookingJpaEntity.bookingJpaEntity;

@Adapter(AdapterType.OUTBOUND)
@RequiredArgsConstructor
public class BookingPersistenceAdapter implements BookingPersistencePort {

    private final BookingJpaRepository bookingJpaRepository;
    private final BookingMapper bookingMapper;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Booking> findBookingByDateAndTimeAndPlace(LocalDate date, String time, String place) {
        return queryFactory
                .selectFrom(bookingJpaEntity)
                .where(bookingJpaEntity.bookingDate.eq(date)
                        .and(bookingJpaEntity.timeSlot.id.timeLabel.eq(time))
                        .and(place != null ? bookingJpaEntity.timeSlot.place.placeName.startsWith(place) : null))
                .fetch()
                .stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> findBookingByDateAndTimeAndPlaceWithLock(LocalDate date, String time, String place) {
        return queryFactory
                .selectFrom(bookingJpaEntity)
                .where(bookingJpaEntity.bookingDate.eq(date)
                        .and(bookingJpaEntity.timeSlot.id.timeLabel.eq(time))
                        .and(place != null ? bookingJpaEntity.timeSlot.place.placeName.startsWith(place) : null))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetch()
                .stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Override
    public Booking findBookingByIdWithLock(Long bookingId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(bookingJpaEntity)
                        .where(bookingJpaEntity.id.eq(bookingId))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        ).map(bookingMapper::toDomain).orElseThrow(BookingNotFoundException::new);
    }

    @Override
    public Boolean existsBookingByDateAndTimeAndPlace(LocalDate date, String time, String place) {
        Integer result = queryFactory
                .selectOne()
                .from(bookingJpaEntity)
                .where(bookingJpaEntity.bookingDate.eq(date)
                        .and(bookingJpaEntity.timeSlot.id.timeLabel.eq(time))
                        .and(bookingJpaEntity.timeSlot.place.placeName.eq(place))
                )
                .fetchFirst();
        return result != null;
    }

    @Override
    public void saveBooking(Booking booking) {
        bookingJpaRepository.save(bookingMapper.toEntity(booking));
    }

    @Override
    public void deleteBookingById(Long bookingId) {
        bookingJpaRepository.deleteById(bookingId);
    }

    @Override
    public void deleteOldBookings() {
        queryFactory
                .delete(bookingJpaEntity)
                .where(bookingJpaEntity.bookingDate.before(LocalDate.now().minusDays(2)))
                .execute();
    }
}