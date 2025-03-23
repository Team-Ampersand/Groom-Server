package com.ampersand.groom.domain.member.persistence.repository;

import com.ampersand.groom.domain.member.persistence.entity.MemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {

    Optional<MemberJpaEntity> findMemberByEmail(String email);
}