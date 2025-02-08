package com.ampersand.groom.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class BaseUuidEntity {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    public UUID id;
}