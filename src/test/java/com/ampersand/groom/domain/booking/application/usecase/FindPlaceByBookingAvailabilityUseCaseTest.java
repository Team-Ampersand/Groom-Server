package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.PlacePersistencePort;
import com.ampersand.groom.domain.booking.domain.Place;
import com.ampersand.groom.domain.booking.persistence.mapper.PlaceMapper;
import com.ampersand.groom.domain.booking.presentation.data.response.GetPlaceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("예약 가능한 장소 조회 UseCase 클래스의")
class FindPlaceByBookingAvailabilityUseCaseTest {

    @Mock
    private PlacePersistencePort placePersistencePort;

    @Mock
    private PlaceMapper placeMapper;

    @InjectMocks
    private FindPlaceByBookingAvailabilityUseCase findPlaceByBookingAvailabilityUseCase;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("예약 가능한 장소가 있을 때")
        class Context_with_available_places {

            @Test
            @DisplayName("장소 리스트를 반환한다.")
            void it_returns_place_list() {
                // Given
                LocalDate date = LocalDate.now();
                String time = "저녁";
                String placeType = "탁구대";
                Place place = mock(Place.class);
                GetPlaceResponse response = mock(GetPlaceResponse.class);
                when(placePersistencePort.findPlaceByBookingAvailability(date, time, placeType))
                        .thenReturn(List.of(place));
                when(placeMapper.toResponse(place))
                        .thenReturn(response);

                // When
                List<GetPlaceResponse> result = findPlaceByBookingAvailabilityUseCase.execute(date, time, placeType);

                // Then
                verify(placePersistencePort).findPlaceByBookingAvailability(date, time, placeType);
                verify(placeMapper).toResponse(place);
                assertEquals(1, result.size());
                assertEquals(response, result.getFirst());
            }
        }

        @Nested
        @DisplayName("예약 가능한 장소가 없을 때")
        class Context_with_no_available_places {

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                // Given
                LocalDate date = LocalDate.now();
                String time = "저녁";
                String placeType = "탁구대";
                when(placePersistencePort.findPlaceByBookingAvailability(date, time, placeType))
                        .thenReturn(List.of());

                // When
                List<GetPlaceResponse> result = findPlaceByBookingAvailabilityUseCase.execute(date, time, placeType);

                // Then
                verify(placePersistencePort).findPlaceByBookingAvailability(date, time, placeType);
                assertTrue(result.isEmpty());
            }
        }
    }
}