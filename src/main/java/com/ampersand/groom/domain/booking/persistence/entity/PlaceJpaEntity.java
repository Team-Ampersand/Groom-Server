package com.ampersand.groom.domain.booking.persistence.entity;

import com.ampersand.groom.global.entity.BaseIdEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "place")
@NoArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "place_id", nullable = false))
public class PlaceJpaEntity extends BaseIdEntity {
    @Size(max = 50)
    @NotNull
    @Column(name = "place_name", nullable = false, length = 50)
    private String placeName;
    @NotNull
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;
    @NotNull
    @ColumnDefault("1")
    @Column(name = "available", nullable = false)
    private Boolean available = false;

    @Builder
    public PlaceJpaEntity(Long id, String placeName, Integer maxCapacity, Boolean available) {
        this.id = id;
        this.placeName = placeName;
        this.maxCapacity = maxCapacity;
        this.available = available;
    }
}