package com.ampersand.groom.domain.booking.persistence.repository;

import com.ampersand.groom.domain.booking.persistence.entity.PlaceJpaEntity;
import com.ampersand.groom.domain.booking.persistence.repository.custom.PlaceJpaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceJpaRepository extends JpaRepository<PlaceJpaEntity, Long>, PlaceJpaRepositoryCustom {
}
