package com.ampersand.groom.domain.booking.persistence;

import com.ampersand.groom.domain.booking.application.port.PlacePersistencePort;
import com.ampersand.groom.domain.booking.domain.Place;
import com.ampersand.groom.domain.booking.persistence.mapper.PlaceMapper;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.ampersand.groom.domain.booking.persistence.entity.QPlaceJpaEntity.placeJpaEntity;
import static com.ampersand.groom.domain.booking.persistence.entity.QTimeSlotJpaEntity.timeSlotJpaEntity;

@RequiredArgsConstructor
@Adapter(AdapterType.OUTBOUND)
public class PlacePersistenceAdapter implements PlacePersistencePort {

    private final PlaceMapper placeMapper;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Place> findPlaceByBookingAvailability(LocalDate date, String time, String placeType) {
        return queryFactory
                .selectFrom(placeJpaEntity)
                .join(timeSlotJpaEntity).on(timeSlotJpaEntity.place.id.eq(placeJpaEntity.id))
                .where(
                        timeSlotJpaEntity.id.timeLabel.eq(time),
                        Expressions.booleanTemplate(
                                "NOT EXISTS (SELECT 1 FROM BookingJpaEntity b " +
                                        "WHERE b.timeSlot.place.id = timeSlotJpaEntity.place.id " +
                                        "AND b.bookingDate = {0} " +
                                        "AND b.timeSlot.id.timeLabel = {1})",
                                date, time
                        ),
                        Objects.nonNull(placeType) ? placeJpaEntity.placeName.startsWith(placeType) : null
                )
                .fetch()
                .stream()
                .map(placeMapper::toDomain)
                .toList();
    }
}