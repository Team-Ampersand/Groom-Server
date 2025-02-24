package com.ampersand.groom.domain.booking.persistence.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TimeSlotJpaEntityId implements Serializable {
    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "time_label")
    private String timeLabel;
}