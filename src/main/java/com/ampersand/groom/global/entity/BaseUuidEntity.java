package com.ampersand.groom.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@MappedSuperclass
abstract class BaseUuidEntity {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    public UUID id;
}