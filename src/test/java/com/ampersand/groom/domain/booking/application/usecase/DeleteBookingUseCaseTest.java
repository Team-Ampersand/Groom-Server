package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.exception.NotBookingPresidentException;
import com.ampersand.groom.domain.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Booking 삭제 UseCase 클래스의")
class DeleteBookingUseCaseTest {

    @Mock
    private BookingPersistencePort bookingPersistencePort;

    @InjectMocks
    private DeleteBookingUseCase deleteBookingUseCase;

    private void mockSecurityContext(String email) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(email);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("회장이 맞을 때")
        class Context_with_correct_president {

            @Test
            @DisplayName("예약을 정상적으로 삭제한다.")
            void it_deletes_booking_successfully() {
                // Given
                Long bookingId = 1L;
                String presidentEmail = "s00001@gsm.hs.kr";
                Member president = Member.builder().email(presidentEmail).build();
                Booking booking = Booking.builder().president(president).build();
                when(bookingPersistencePort.findBookingByIdWithLock(bookingId)).thenReturn(booking);
                mockSecurityContext(presidentEmail);

                // When
                deleteBookingUseCase.execute(bookingId);

                // Then
                verify(bookingPersistencePort).deleteBookingById(bookingId);
            }
        }

        @Nested
        @DisplayName("대표자가 아닐 때")
        class Context_with_wrong_president {

            @Test
            @DisplayName("NotBookingPresidentException을 발생시킨다.")
            void it_throws_NotBookingPresidentException() {
                // Given
                Long bookingId = 1L;
                String actualPresidentEmail = "s00001@gsm.hs.kr";
                String otherUserEmail = "s00002@gsm.hs.kr";
                Member president = Member.builder().email(actualPresidentEmail).build();
                Booking booking = Booking.builder().president(president).build();
                when(bookingPersistencePort.findBookingByIdWithLock(bookingId)).thenReturn(booking);
                mockSecurityContext(otherUserEmail);

                // When & Then
                assertThrows(NotBookingPresidentException.class, () -> deleteBookingUseCase.execute(bookingId));
            }
        }
    }
}