package com.ampersand.groom.domain.booking.persistence;

import com.ampersand.groom.domain.booking.application.port.TimeSlotPersistencePort;
import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.persistence.mapper.TimeSlotMapper;
import com.ampersand.groom.domain.booking.persistence.repository.TimeSlotJpaRepository;
import com.ampersand.groom.global.annotation.adapter.Adapter;
import com.ampersand.groom.global.annotation.adapter.constant.AdapterType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Adapter(AdapterType.OUTBOUND)
@RequiredArgsConstructor
public class TimeSlotPersistenceAdapter implements TimeSlotPersistencePort {

    private final TimeSlotJpaRepository timeSlotJpaRepository;
    private final TimeSlotMapper timeSlotMapper;

    @Override
    @Cacheable(value = "timeSlots")
    public List<TimeSlot> findAllTimeSlots() {
        return timeSlotJpaRepository.findAll().stream()
                .map(timeSlotMapper::toDomain)
                .toList();
    }
}
