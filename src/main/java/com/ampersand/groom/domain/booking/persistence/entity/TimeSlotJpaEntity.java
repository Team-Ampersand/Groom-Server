package com.ampersand.groom.domain.booking.persistence.entity;

import com.ampersand.groom.global.entity.BaseIdEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "time_slot", uniqueConstraints = {
        @UniqueConstraint(name = "unique_time_slot", columnNames = {"place_id", "time_label"})
})
@AttributeOverride(name = "id", column = @Column(name = "time_slot_id", nullable = false))
@NoArgsConstructor
public class TimeSlotJpaEntity extends BaseIdEntity {

    @Size(max = 20)
    @NotNull
    @Column(name = "time_label", nullable = false, length = 20)
    private String timeLabel;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    private PlaceJpaEntity place;

    @Builder
    public TimeSlotJpaEntity(Long id, String timeLabel, PlaceJpaEntity place) {
        this.id = id;
        this.timeLabel = timeLabel;
        this.place = place;
    }
}