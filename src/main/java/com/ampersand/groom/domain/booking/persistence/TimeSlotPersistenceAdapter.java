package com.ampersand.groom.domain.booking.persistence;

import com.ampersand.groom.domain.booking.application.port.TimeSlotPersistencePort;
import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.persistence.mapper.TimeSlotMapper;
import com.ampersand.groom.domain.booking.persistence.repository.TimeSlotJpaRepository;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adapter(AdapterType.OUTBOUND)
@RequiredArgsConstructor
public class TimeSlotPersistenceAdapter implements TimeSlotPersistencePort {

    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final TimeSlotMapper timeSlotMapper;

    @Override
    public List<TimeSlot> findTimeSlotByPlace(String place) {
        return timeSlotJpaRepository.findTimeSlotsByPlace(place).stream()
                .map(timeSlotMapper::toDomain)
                .toList();
    }
}
