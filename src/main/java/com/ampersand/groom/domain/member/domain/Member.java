package com.ampersand.groom.domain.member.domain;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Integer generation;
    private final Boolean isAvailable;
    private final MemberRole role;
}