package com.ampersand.groom.domain.booking.persistence.mapper;

import com.ampersand.groom.domain.booking.domain.Place;
import com.ampersand.groom.domain.booking.persistence.entity.PlaceJpaEntity;
import com.ampersand.groom.domain.booking.presentation.data.response.GetPlaceResponse;
import com.ampersand.groom.global.mapper.GenericMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlaceMapper implements GenericMapper<PlaceJpaEntity, Place> {
    @Override
    public Place toDomain(PlaceJpaEntity placeJpaEntity) {
        return Place.builder()
                .placeName(placeJpaEntity.getPlaceName())
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

    public GetPlaceResponse toResponse(Place place) {
        return new GetPlaceResponse(
                place.getPlaceName(),
                place.getMaxCapacity()
        );
    }
}