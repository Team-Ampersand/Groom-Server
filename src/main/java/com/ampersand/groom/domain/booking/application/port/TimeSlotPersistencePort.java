package com.ampersand.groom.domain.booking.application.port;

import com.ampersand.groom.domain.booking.domain.TimeSlot;

import java.util.List;

public interface TimeSlotPersistencePort {
    List<TimeSlot> findAllTimeSlots();
}