package com.ampersand.groom.domain.auth.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "auth_code")
@Getter
@NoArgsConstructor
public class AuthCodeRedisEntity {
    @Id
    private String email;
    @Indexed
    private String code;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;

    @Builder
    public AuthCodeRedisEntity(String email, String code, Long ttl) {
        this.email = email;
        this.code = code;
        this.ttl = ttl;
    }
}