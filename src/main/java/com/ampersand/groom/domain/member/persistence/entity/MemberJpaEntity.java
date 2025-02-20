package com.ampersand.groom.domain.member.persistence.entity;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.global.entity.BaseIdEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member")
public class MemberJpaEntity extends BaseIdEntity {
    @Column(nullable = false, length = 10)
    private String name;
    @Column(nullable = false, unique = true, length = 30)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Integer generation;
    @Column(name = "available", nullable = false)
    private Boolean isAvailable;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Builder
    public MemberJpaEntity(Long id, String name, String email, String password, Integer generation, Boolean isAvailable, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.generation = generation;
        this.isAvailable = isAvailable;
        this.role = role;
    }
}