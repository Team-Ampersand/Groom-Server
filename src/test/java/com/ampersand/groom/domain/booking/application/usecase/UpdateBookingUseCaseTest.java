package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.booking.application.port.TimeSlotPersistencePort;
import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.domain.Place;
import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.domain.constant.TimeSlotId;
import com.ampersand.groom.domain.booking.exception.DuplicateBookingException;
import com.ampersand.groom.domain.booking.exception.MaxCapacityExceededException;
import com.ampersand.groom.domain.booking.exception.NotBookingDateException;
import com.ampersand.groom.domain.booking.exception.NotBookingPresidentException;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateBookingUseCase 클래스의")
class UpdateBookingUseCaseTest {

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private BookingPersistencePort bookingPersistencePort;

    @Mock
    private TimeSlotPersistencePort timeSlotPersistencePort;

    @InjectMocks
    private UpdateBookingUseCase updateBookingUseCase;

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
        @DisplayName("예약이 유효할 때")
        class Context_with_valid_booking {

            @Test
            @DisplayName("예약을 성공적으로 업데이트한다.")
            void it_updates_booking_successfully() {
                // Given
                Long bookingId = 1L;
                String time = "저녁";
                String place = "탁구대";
                List<Long> participantIds = List.of(3L, 4L);
                Member president = Member.builder().id(2L).email("s00001@gsm.hs.kr").build();
                Booking booking = Booking.builder()
                        .id(bookingId)
                        .president(president)
                        .bookingDate(LocalDate.now())
                        .build();
                when(bookingPersistencePort.findBookingByIdWithLock(bookingId)).thenReturn(booking);
                when(memberPersistencePort.findMemberByEmail("s00001@gsm.hs.kr")).thenReturn(president);
                when(timeSlotPersistencePort.findAllTimeSlots()).thenReturn(List.of(
                        TimeSlot.builder()
                                .timeSlotId(new TimeSlotId(1L, time))
                                .place(Place.builder().placeName(place).maxCapacity(4).build())
                                .build()
                ));
                when(bookingPersistencePort.ExistsBookingByDateAndTimeAndPlace(any(), any(), any())).thenReturn(false);
                when(memberPersistencePort.findMembersByIds(participantIds)).thenReturn(List.of(
                        Member.builder().id(3L).build(),
                        Member.builder().id(4L).build()
                ));
                mockSecurityContext("s00001@gsm.hs.kr");

                // When
                updateBookingUseCase.execute(bookingId, time, place, participantIds);

                // Then
                verify(bookingPersistencePort).saveBooking(any());
            }
        }

        @Nested
        @DisplayName("예약 날짜가 현재보다 이전일 때")
        class Context_with_past_booking_date {

            @Test
            @DisplayName("NotBookingDateException을 발생시킨다.")
            void it_throws_NotBookingDateException() {
                // Given
                Long bookingId = 1L;
                when(bookingPersistencePort.findBookingByIdWithLock(bookingId)).thenReturn(
                        Booking.builder().bookingDate(LocalDate.now().minusDays(1)).build()
                );

                // When & Then
                assertThrows(NotBookingDateException.class, () ->
                        updateBookingUseCase.execute(bookingId, "저녁", "탁구대", List.of(3L, 4L))
                );
            }
        }

        @Nested
        @DisplayName("예약을 수정하려는 사용자가 대표자가 아닐 때")
        class Context_with_non_president_user {

            @Test
            @DisplayName("NotBookingPresidentException을 발생시킨다.")
            void it_throws_NotBookingPresidentException() {
                // Given
                Long bookingId = 1L;
                Member president = Member.builder().id(2L).email("s00001@gsm.hs.kr").build();
                Member otherUser = Member.builder().id(99L).email("s00099@gsm.hs.kr").build();
                when(bookingPersistencePort.findBookingByIdWithLock(bookingId)).thenReturn(
                        Booking.builder().president(president).bookingDate(LocalDate.now()).build()
                );
                when(memberPersistencePort.findMemberByEmail("s00002@gsm.hs.kr")).thenReturn(otherUser);
                mockSecurityContext("s00002@gsm.hs.kr");

                // When & Then
                assertThrows(NotBookingPresidentException.class, () ->
                        updateBookingUseCase.execute(bookingId, "저녁", "탁구대", List.of(3L, 4L))
                );
            }
        }

        @Nested
        @DisplayName("해당 시간과 장소에 예약이 이미 존재할 때")
        class Context_with_duplicate_booking {

            @Test
            @DisplayName("DuplicateBookingException을 발생시킨다.")
            void it_throws_DuplicateBookingException() {
                // Given
                Long bookingId = 1L;
                String time = "저녁";
                String place = "탁구대";
                when(bookingPersistencePort.ExistsBookingByDateAndTimeAndPlace(any(), any(), any())).thenReturn(true);
                when(bookingPersistencePort.findBookingByIdWithLock(bookingId)).thenReturn(
                        Booking.builder().president(Member.builder().id(2L).email("s00001@gsm.hs.kr").build()).bookingDate(LocalDate.now()).build()
                );
                when(timeSlotPersistencePort.findAllTimeSlots()).thenReturn(List.of(
                        TimeSlot.builder().place(Place.builder().id(1L).placeName(place).maxCapacity(4).build()).timeSlotId(new TimeSlotId(1L, time)).build()
                ));
                when(memberPersistencePort.findMemberByEmail("s00001@gsm.hs.kr")).thenReturn(
                        Member.builder().id(2L).email("000001@gsm.hs.kr").build()
                );
                mockSecurityContext("s00001@gsm.hs.kr");

                // When & Then
                assertThrows(DuplicateBookingException.class, () ->
                        updateBookingUseCase.execute(bookingId, "저녁", "탁구대", List.of(3L, 4L))
                );
            }
        }

        @Nested
        @DisplayName("참가자가 최대 인원을 초과했을 때")
        class Context_with_exceeded_max_capacity {

            @Test
            @DisplayName("MaxCapacityExceededException을 발생시킨다.")
            void it_throws_MaxCapacityExceededException() {
                // Given
                Long bookingId = 1L;
                String time = "저녁";
                String place = "탁구대";
                when(bookingPersistencePort.findBookingByIdWithLock(bookingId)).thenReturn(
                        Booking.builder().president(Member.builder().id(2L).email("s00001@gsm.hs.kr").build()).bookingDate(LocalDate.now()).build()
                );
                when(timeSlotPersistencePort.findAllTimeSlots()).thenReturn(List.of(
                        TimeSlot.builder().place(Place.builder().id(1L).placeName(place).maxCapacity(4).build()).timeSlotId(new TimeSlotId(1L, time)).build()
                ));
                when(memberPersistencePort.findMemberByEmail("s00001@gsm.hs.kr")).thenReturn(
                        Member.builder().id(2L).email("000001@gsm.hs.kr").build()
                );
                mockSecurityContext("s00001@gsm.hs.kr");

                // When & Then
                assertThrows(MaxCapacityExceededException.class, () ->
                        updateBookingUseCase.execute(bookingId, "저녁", "탁구대", List.of(3L, 4L, 5L, 6L))
                );
            }
        }
    }
}