package com.ampersand.groom.domain.booking.persistence.mapper;

import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.domain.constant.TimeSlotId;
import com.ampersand.groom.domain.booking.persistence.entity.TimeSlotJpaEntity;
import com.ampersand.groom.domain.booking.persistence.entity.id.TimeSlotJpaEntityId;
import com.ampersand.groom.global.mapper.GenericMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeSlotMapper implements GenericMapper<TimeSlotJpaEntity, TimeSlot> {

    private final PlaceMapper placeMapper;

    @Override
    public TimeSlot toDomain(TimeSlotJpaEntity timeSlotJpaEntity) {
            return TimeSlot.builder()
                    .timeSlotId(toDomainTimeSlotId(timeSlotJpaEntity.getId()))
                    .place(placeMapper.toDomain(timeSlotJpaEntity.getPlace()))
                    .build();

    }

    @Override
    public TimeSlotJpaEntity toEntity(TimeSlot timeSlot) {
        return TimeSlotJpaEntity.builder()
                .id(toEntityTimeSlotId(timeSlot.getTimeSlotId()))
                .place(placeMapper.toEntity(timeSlot.getPlace()))
                .build();
    }

    private TimeSlotJpaEntityId toEntityTimeSlotId(TimeSlotId timeSlotId) {
        return new TimeSlotJpaEntityId(
                timeSlotId.placeId(),
                timeSlotId.timeLabel()
        );
    }

    private TimeSlotId toDomainTimeSlotId(TimeSlotJpaEntityId timeSlotJpaEntityId) {
        return new TimeSlotId(
                timeSlotJpaEntityId.getPlaceId(),
                timeSlotJpaEntityId.getTimeLabel()
        );
    }
}
