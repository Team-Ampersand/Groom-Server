package com.ampersand.groom.domain.booking.persistence.repository.custom;

import com.ampersand.groom.domain.booking.persistence.entity.PlaceJpaEntity;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ampersand.groom.domain.booking.persistence.entity.QPlaceJpaEntity.placeJpaEntity;

@Repository
@RequiredArgsConstructor
public class PlaceJpaRepositoryCustomImpl implements PlaceJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceJpaEntity> findPlaceByBookingAvailability(LocalDate date, String time, String placeType) {
        return queryFactory
                .selectFrom(placeJpaEntity)
                .where(
                        Expressions.booleanTemplate(
                                "NOT EXISTS (SELECT 1 FROM BookingJpaEntity b " +
                                        "WHERE timeSlot.id.placeId = {0} " +
                                        "AND b.bookingDate = {1} " +
                                        "AND b.timeSlot.id.timeLabel = {2})",
                                placeJpaEntity.id, date, time
                        ),
                        Objects.nonNull(placeType) ? placeJpaEntity.placeName.startsWith(placeType) : null
                )
                .fetch();
    }

    @Override
    public Optional<PlaceJpaEntity> findPlaceByPlaceName(String placeName) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(placeJpaEntity)
                        .where(placeJpaEntity.placeName.eq(placeName))
                        .fetchOne()
        );
    }
}
