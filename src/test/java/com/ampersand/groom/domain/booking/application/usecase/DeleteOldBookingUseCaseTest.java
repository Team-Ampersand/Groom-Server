package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("오래된 Booking 삭제 UseCase 클래스의")
class DeleteOldBookingUseCaseTest {

    @Mock
    private BookingPersistencePort bookingPersistencePort;

    @InjectMocks
    private DeleteOldBookingUseCase deleteOldBookingUseCase;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("오래된 예약이 존재할 때")
        class Context_with_old_bookings {

            @Test
            @DisplayName("오래된 예약을 삭제한다.")
            void it_deletes_old_bookings() {
                // When
                deleteOldBookingUseCase.execute();

                // Then
                verify(bookingPersistencePort, times(1)).deleteOldBookings();
            }
        }

        @Nested
        @DisplayName("오래된 예약이 존재하지 않을 때")
        class Context_without_old_bookings {

            @Test
            @DisplayName("실제론 삭제할 데이터가 없더라도 메서드 호출을 완료한다.")
            void it_invokes_deleteOldBookings_but_nothing_happens() {
                // Given
                doNothing().when(bookingPersistencePort).deleteOldBookings();

                // When
                deleteOldBookingUseCase.execute();

                // Then
                verify(bookingPersistencePort, times(1)).deleteOldBookings();
            }
        }
    }
}