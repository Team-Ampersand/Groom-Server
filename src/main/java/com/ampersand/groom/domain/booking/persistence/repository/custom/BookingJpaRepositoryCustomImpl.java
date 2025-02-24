package com.ampersand.groom.domain.booking.persistence.repository.custom;

import com.ampersand.groom.domain.booking.persistence.entity.BookingJpaEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
}