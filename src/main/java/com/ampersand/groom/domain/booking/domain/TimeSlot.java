package com.ampersand.groom.domain.booking.domain;

import com.ampersand.groom.domain.booking.domain.constant.TimeSlotId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimeSlot {
    private final TimeSlotId timeSlotId;
    private final Place place;
}