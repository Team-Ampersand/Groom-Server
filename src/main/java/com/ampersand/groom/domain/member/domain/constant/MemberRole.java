package com.ampersand.groom.domain.member.domain.constant;

import org.springframework.security.core.GrantedAuthority;

public enum MemberRole implements GrantedAuthority {
    ROLE_ADMIN, ROLE_STUDENT, ROLE_TEACHER;

    @Override
    public String getAuthority() {
        return name();
    }
}