package com.ampersand.groom.domain.auth.persistence.repository;

import com.ampersand.groom.domain.auth.persistence.entity.AuthenticationRedisEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthenticationRedisRepository extends CrudRepository<AuthenticationRedisEntity, String> {
    Optional<AuthenticationRedisEntity> findByEmail(String email);
}