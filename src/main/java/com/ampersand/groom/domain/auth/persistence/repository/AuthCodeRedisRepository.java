package com.ampersand.groom.domain.auth.persistence.repository;

import com.ampersand.groom.domain.auth.persistence.entity.AuthCodeRedisEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthCodeRedisRepository extends CrudRepository<AuthCodeRedisEntity, String> {
    AuthCodeRedisEntity findByCode(String code);

    Boolean existsByCode(String code);

    void deleteByCode(String code);
}