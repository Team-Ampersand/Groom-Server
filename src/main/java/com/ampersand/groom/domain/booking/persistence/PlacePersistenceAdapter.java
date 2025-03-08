package com.ampersand.groom.domain.booking.persistence;

import com.ampersand.groom.domain.booking.application.port.PlacePersistencePort;
import com.ampersand.groom.domain.booking.domain.Place;
import com.ampersand.groom.domain.booking.persistence.mapper.PlaceMapper;
import com.ampersand.groom.domain.booking.persistence.repository.PlaceJpaRepository;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.ampersand.groom.domain.booking.persistence.entity.QPlaceJpaEntity.placeJpaEntity;

@RequiredArgsConstructor
@Adapter(AdapterType.OUTBOUND)
public class PlacePersistenceAdapter implements PlacePersistencePort {

    private final PlaceJpaRepository placeJpaRepository;
    private final PlaceMapper placeMapper;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Place> findPlaceByBookingAvailability(LocalDate date, String time, String placeType) {
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
                .fetch()
                .stream()
                .map(placeMapper::toDomain)
                .toList();
    }
}