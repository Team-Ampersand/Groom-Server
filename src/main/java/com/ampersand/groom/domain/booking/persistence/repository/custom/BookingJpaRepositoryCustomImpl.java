package com.ampersand.groom.domain.booking.persistence.repository.custom;

import com.ampersand.groom.domain.booking.persistence.entity.BookingJpaEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ampersand.groom.domain.booking.persistence.entity.QBookingJpaEntity.bookingJpaEntity;

@Repository
@RequiredArgsConstructor
public class BookingJpaRepositoryCustomImpl implements BookingJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookingJpaEntity> findBookingByDateAndTimeAndPlace(LocalDate date, String time, String placeType) {
        return queryFactory
                .selectFrom(bookingJpaEntity)
                .where(bookingJpaEntity.bookingDate.eq(date)
                        .and(bookingJpaEntity.timeSlot.id.timeLabel.eq(time))
                        .and(placeType != null ? bookingJpaEntity.timeSlot.place.placeName.startsWith(placeType) : null))
                .fetch();
    }

    @Override
    public Optional<BookingJpaEntity> findByIdWithLock(Long bookingId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(bookingJpaEntity)
                        .where(bookingJpaEntity.id.eq(bookingId))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        );
    }

    @Override
    public Boolean ExistsBookingByDateAndTimeAndPlace(LocalDate date, String time, String place) {
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
}