package com.ampersand.groom.domain.booking.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimeSlot {
    private final Long id;
    private final String timeLabel;
    private final Place place;
}
