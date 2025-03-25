package com.ampersand.groom.domain.auth.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthCode {
    private final String email;
    private final String code;
    private final Long ttl;
}