package com.ampersand.groom.domain.booking.persistence.mapper;

import com.ampersand.groom.domain.booking.domain.Place;
import com.ampersand.groom.domain.booking.persistence.entity.PlaceJpaEntity;
import com.ampersand.groom.global.mapper.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class PlaceMapper implements GenericMapper<PlaceJpaEntity, Place>{
    @Override
    public Place toDomain(PlaceJpaEntity placeJpaEntity) {
        return Place.builder()
                .id(placeJpaEntity.getId())
                .maxCapacity(placeJpaEntity.getMaxCapacity())
                .isAvailable(placeJpaEntity.getAvailable())
                .build();
    }

    @Override
    public PlaceJpaEntity toEntity(Place place) {
        return PlaceJpaEntity.builder()
                .id(place.getId())
                .placeName(place.getPlaceName())
                .maxCapacity(place.getMaxCapacity())
                .available(place.getIsAvailable())
                .build();
    }
}
