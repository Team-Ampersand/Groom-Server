package com.ampersand.groom.domain.booking.persistence.entity;

import com.ampersand.groom.domain.booking.persistence.entity.id.TimeSlotJpaEntityId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "time_slot")
@AttributeOverride(name = "id", column = @Column(name = "time_slot_id", nullable = false))
@NoArgsConstructor
public class TimeSlotJpaEntity {

    @EmbeddedId
    private TimeSlotJpaEntityId id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", insertable = false, updatable = false)
    private PlaceJpaEntity place;


    @Builder
    public TimeSlotJpaEntity(TimeSlotJpaEntityId id, PlaceJpaEntity place) {
        this.id = id;
        this.place = place;
    }
}