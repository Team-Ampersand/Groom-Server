package com.ampersand.groom.domain.booking.persistence.repository;

import com.ampersand.groom.domain.booking.persistence.entity.BookingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingJpaRepository extends JpaRepository<BookingJpaEntity, Long> {
}