package com.ampersand.groom.domain.booking.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Place {
    private final Long id;
    private final String placeName;
    private final Integer maxCapacity;
    private final Boolean isAvailable;
}