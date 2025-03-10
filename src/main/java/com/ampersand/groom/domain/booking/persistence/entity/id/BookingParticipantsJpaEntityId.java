package com.ampersand.groom.domain.booking.persistence.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class BookingParticipantsJpaEntityId implements Serializable {
    @NotNull
    @Column(name = "booking_id", nullable = false)
    private Integer bookingId;

    @NotNull
    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookingParticipantsJpaEntityId entity = (BookingParticipantsJpaEntityId) o;
        return Objects.equals(this.bookingId, entity.bookingId) &&
                Objects.equals(this.memberId, entity.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, memberId);
    }

}