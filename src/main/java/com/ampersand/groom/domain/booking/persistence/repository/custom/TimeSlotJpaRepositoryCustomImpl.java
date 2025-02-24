package com.ampersand.groom.domain.booking.persistence.repository.custom;

import com.ampersand.groom.domain.booking.persistence.entity.TimeSlotJpaEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ampersand.groom.domain.booking.persistence.entity.QTimeSlotJpaEntity.timeSlotJpaEntity;

@Repository
@RequiredArgsConstructor
public class TimeSlotJpaRepositoryCustomImpl implements TimeSlotJpaRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TimeSlotJpaEntity> findTimeSlotsByPlace(String place) {
        return queryFactory
                .selectFrom(timeSlotJpaEntity)
                .where(timeSlotJpaEntity.place.placeName.eq(place))
                .fetch();
    }
}
