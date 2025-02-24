package com.ampersand.groom.domain.booking.persistence.repository;

import com.ampersand.groom.domain.booking.persistence.entity.BookingJpaEntity;
import com.ampersand.groom.domain.booking.persistence.repository.custom.BookingJpaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingJpaRepository extends JpaRepository<BookingJpaEntity, Long>, BookingJpaRepositoryCustom {
}