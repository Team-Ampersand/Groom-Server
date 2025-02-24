package com.ampersand.groom.domain.booking.persistence.repository.custom;

import com.ampersand.groom.domain.booking.persistence.entity.TimeSlotJpaEntity;

import java.util.List;

public interface TimeSlotJpaRepositoryCustom {
    List<TimeSlotJpaEntity> findTimeSlotsByPlace(String place);
}