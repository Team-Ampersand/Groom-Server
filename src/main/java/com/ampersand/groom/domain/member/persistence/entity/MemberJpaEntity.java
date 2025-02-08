package com.ampersand.groom.domain.member.persistence.entity;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.global.entity.BaseUuidEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member")
@ToString
public class MemberJpaEntity extends BaseUuidEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    @ToString.Exclude
    private String password;
    @Column(nullable = false)
    private Integer generation;
    @Column(nullable = false, name = "available")
    private Boolean isAvailable;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public MemberJpaEntity(UUID id, String name, String email, String password, Integer generation, Boolean isAvailable, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.generation = generation;
        this.isAvailable = isAvailable;
        this.role = role;
    }
}