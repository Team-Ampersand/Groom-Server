package com.ampersand.groom.domain.member.domain;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Member {
    private final UUID id;
    private final String name;
    private final String email;
    private final String password;
    private final Integer generation;
    private final Boolean isAvailable;
    private final MemberRole role;
}