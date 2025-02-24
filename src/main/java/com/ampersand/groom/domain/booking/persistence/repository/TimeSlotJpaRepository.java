package com.ampersand.groom.domain.booking.persistence.repository;

import com.ampersand.groom.domain.booking.persistence.entity.TimeSlotJpaEntity;
import com.ampersand.groom.domain.booking.persistence.repository.custom.TimeSlotJpaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotJpaRepository extends JpaRepository<TimeSlotJpaEntity, Long>, TimeSlotJpaRepositoryCustom {
}