package com.ampersand.groom.domain.booking.persistence.entity;

import com.ampersand.groom.domain.member.persistence.entity.MemberJpaEntity;
import com.ampersand.groom.global.entity.BaseIdEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "booking")
@AttributeOverride(name = "id", column = @Column(name = "booking_id", nullable = false))
@NoArgsConstructor
public class BookingJpaEntity extends BaseIdEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "president_id")
    private MemberJpaEntity president;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TimeSlotJpaEntity timeSlot;

    @NotNull
    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "booking_participants",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<MemberJpaEntity> participants = new HashSet<>();

    @Builder
    public BookingJpaEntity(Long id, MemberJpaEntity president, TimeSlotJpaEntity timeSlot, LocalDate bookingDate, Set<MemberJpaEntity> participants) {
        this.id = id;
        this.president = president;
        this.timeSlot = timeSlot;
        this.bookingDate = bookingDate;
        this.participants = participants;
    }
}