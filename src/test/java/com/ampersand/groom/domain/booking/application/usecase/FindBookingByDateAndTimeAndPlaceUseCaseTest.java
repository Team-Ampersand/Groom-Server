package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.persistence.BookingPersistenceAdapter;
import com.ampersand.groom.domain.booking.persistence.mapper.BookingMapper;
import com.ampersand.groom.domain.booking.presentation.data.response.GetBookingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Booking 검색 UseCase 클래스의")
class FindBookingByDateAndTimeAndPlaceUseCaseTest {

    @Mock
    private BookingPersistenceAdapter bookingPersistenceAdapter;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private FindBookingByDateAndTimeAndPlaceUseCase findBookingUseCase;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("예약이 존재할 때")
        class Context_with_existing_bookings {

            @Test
            @DisplayName("예약 리스트를 반환한다.")
            void it_returns_booking_list() {
                // Given
                LocalDate date = LocalDate.now();
                String time = "저녁";
                String place = "탁구대";
                Booking booking = mock(Booking.class);
                GetBookingResponse response = mock(GetBookingResponse.class);
                when(bookingPersistenceAdapter.findBookingByDateAndTimeAndPlace(date, time, place))
                        .thenReturn(List.of(booking));
                when(bookingMapper.toResponse(booking))
                        .thenReturn(response);

                // When
                List<GetBookingResponse> result = findBookingUseCase.execute(date, time, place);

                // Then
                verify(bookingPersistenceAdapter).findBookingByDateAndTimeAndPlace(date, time, place);
                verify(bookingMapper).toResponse(booking);
                assertEquals(1, result.size());
                assertEquals(response, result.getFirst());
            }
        }

        @Nested
        @DisplayName("예약이 존재하지 않을 때")
        class Context_with_no_bookings {

            @Test
            @DisplayName("빈 리스트를 반환한다.")
            void it_returns_empty_list() {
                // Given
                LocalDate date = LocalDate.now();
                String time = "저녁";
                String place = "탁구대";
                when(bookingPersistenceAdapter.findBookingByDateAndTimeAndPlace(date, time, place))
                        .thenReturn(List.of());

                // When
                List<GetBookingResponse> result = findBookingUseCase.execute(date, time, place);

                // Then
                verify(bookingPersistenceAdapter).findBookingByDateAndTimeAndPlace(date, time, place);
                assertTrue(result.isEmpty());
            }
        }

        @Nested
        @DisplayName("매개변수 중 place가 null 일 때")
        class Context_with_null_place {

            @Test
            @DisplayName("예약 리스트를 반환한다.")
            void it_returns_booking_list_when_place_is_null() {
                // Given
                LocalDate date = LocalDate.now();
                String time = "저녁";
                String place = null;
                Booking booking = mock(Booking.class);
                GetBookingResponse response = mock(GetBookingResponse.class);
                when(bookingPersistenceAdapter.findBookingByDateAndTimeAndPlace(date, time, place))
                        .thenReturn(List.of(booking));
                when(bookingMapper.toResponse(booking))
                        .thenReturn(response);

                // When
                List<GetBookingResponse> result = findBookingUseCase.execute(date, time, place);

                // Then
                verify(bookingPersistenceAdapter).findBookingByDateAndTimeAndPlace(date, time, place);
                verify(bookingMapper).toResponse(booking);
                assertEquals(1, result.size());
                assertEquals(response, result.getFirst());
            }
        }
    }
}