package com.ampersand.groom.domain.booking.persistence.mapper;

import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.persistence.entity.TimeSlotJpaEntity;
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
                .id(timeSlotJpaEntity.getId())
                .timeLabel(timeSlotJpaEntity.getTimeLabel())
                .place(placeMapper.toDomain(timeSlotJpaEntity.getPlace()))
                .build();
    }

    @Override
    public TimeSlotJpaEntity toEntity(TimeSlot timeSlot) {
        return TimeSlotJpaEntity.builder()
                .id(timeSlot.getId())
                .timeLabel(timeSlot.getTimeLabel())
                .place(placeMapper.toEntity(timeSlot.getPlace()))
                .build();
    }
}
