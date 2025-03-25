package com.ampersand.groom.domain.auth.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Authentication {
    private String email;
    private int attemptCount;
    private Boolean verified;
    private Long ttl;
}