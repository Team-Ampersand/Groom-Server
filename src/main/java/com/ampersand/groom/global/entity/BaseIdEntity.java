package com.ampersand.groom.global.entity;

import jakarta.persistence.*;

@MappedSuperclass
abstract class BaseIdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
}
