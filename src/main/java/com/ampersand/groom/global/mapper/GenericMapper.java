package com.ampersand.groom.global.mapper;

public interface GenericMapper<ENTITY, DOMAIN> {
    ENTITY toEntity(DOMAIN domain);

    DOMAIN toDomain(ENTITY entity);
}