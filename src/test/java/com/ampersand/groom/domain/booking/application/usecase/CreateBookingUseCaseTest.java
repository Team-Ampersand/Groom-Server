package com.ampersand.groom.domain.booking.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.booking.application.port.TimeSlotPersistencePort;
import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.domain.Place;
import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.domain.constant.TimeSlotId;
import com.ampersand.groom.domain.booking.exception.DuplicateBookingException;
import com.ampersand.groom.domain.booking.exception.InvalidBookingInfomationException;
import com.ampersand.groom.domain.booking.exception.InvalidBookingParticipantsException;
import com.ampersand.groom.domain.booking.exception.MaxCapacityExceededException;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Booking 생성 UseCase 클래스의")
class CreateBookingUseCaseTest {

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private BookingPersistencePort bookingPersistencePort;

    @Mock
    private TimeSlotPersistencePort timeSlotPersistencePort;

    @InjectMocks
    private CreateBookingUseCase createBookingUseCase;

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("유효한 예약 정보가 주어졌을 때")
        class Context_with_valid_booking_information {

            @Test
            @DisplayName("예약을 생성한다.")
            void it_creates_booking() {
                // Given
                String time = "저녁";
                String place = "탁구대";
                List<Long> participantIds = List.of(3L, 4L);
                Place placeEntity = Place.builder()
                        .id(1L)
                        .isAvailable(true)
                        .placeName(place)
                        .maxCapacity(4)
                        .build();
                TimeSlotId timeSlotId = new TimeSlotId(1L, time);
                TimeSlot timeSlot = TimeSlot.builder()
                        .timeSlotId(timeSlotId)
                        .place(placeEntity)
                        .build();
                Member president = Member.builder()
                        .id(2L)
                        .role(MemberRole.ROLE_STUDENT)
                        .email("s00002@gsm.hs.kr")
                        .generation(8)
                        .name("성춘향")
                        .isAvailable(true)
                        .build();
                List<Member> participants = List.of(
                        Member.builder().id(3L).name("참가자1").build(),
                        Member.builder().id(4L).name("참가자2").build()
                );
                when(timeSlotPersistencePort.findAllTimeSlots()).thenReturn(List.of(timeSlot));
                when(bookingPersistencePort.findBookingByDateAndTimeAndPlace(any(), any(), any()))
                        .thenReturn(List.of());
                when(memberPersistencePort.findMemberById(2L)).thenReturn(president);
                when(memberPersistencePort.findMembersByIds(participantIds)).thenReturn(participants);

                // TODO: JWT에서 현재 사용자 정보 가져오도록 수정 필요

                // When
                createBookingUseCase.execute(time, place, participantIds);

                // Then
                verify(bookingPersistencePort).saveBooking(any());
            }
        }

        @Nested
        @DisplayName("참가 인원이 최대 인원을 초과했을 때")
        class Context_with_exceeded_max_capacity {

            @Test
            @DisplayName("MaxCapacityExceededException을 발생시킨다.")
            void it_throws_MaxCapacityExceededException() {
                // Given
                String time = "저녁";
                String place = "탁구대";
                List<Long> participantIds = List.of(3L, 4L, 5L, 6L);
                Place placeEntity = Place.builder()
                        .id(1L)
                        .placeName(place)
                        .isAvailable(true)
                        .maxCapacity(4)
                        .build();
                TimeSlotId timeSlotId = new TimeSlotId(1L, time);
                TimeSlot timeSlot = TimeSlot.builder()
                        .timeSlotId(timeSlotId)
                        .place(placeEntity)
                        .build();

                when(timeSlotPersistencePort.findAllTimeSlots()).thenReturn(List.of(timeSlot));

                // When & Then
                assertThrows(MaxCapacityExceededException.class, () ->
                        createBookingUseCase.execute(time, place, participantIds)
                );
            }
        }

        @Nested
        @DisplayName("동일한 시간과 장소에 예약이 존재할 때")
        class Context_with_duplicate_booking {

            @Test
            @DisplayName("DuplicateBookingException을 발생시킨다.")
            void it_throws_DuplicateBookingException() {
                // Given
                String time = "저녁";
                String place = "탁구대";
                List<Long> participantIds = List.of(3L, 4L);
                Place placeEntity = Place.builder()
                        .id(1L)
                        .isAvailable(true)
                        .placeName(place)
                        .maxCapacity(4)
                        .build();
                TimeSlotId timeSlotId = new TimeSlotId(1L, time);
                TimeSlot timeSlot = TimeSlot.builder()
                        .timeSlotId(timeSlotId)
                        .place(placeEntity)
                        .build();
                when(timeSlotPersistencePort.findAllTimeSlots()).thenReturn(List.of(timeSlot));
                when(bookingPersistencePort.findBookingByDateAndTimeAndPlace(any(), any(), any()))
                        .thenReturn(List.of(mock(Booking.class)));

                // When & Then
                assertThrows(DuplicateBookingException.class, () ->
                        createBookingUseCase.execute(time, place, participantIds)
                );
            }
        }

        @Nested
        @DisplayName("참가자 정보에 문제가 있을 때")
        class Context_with_invalid_participants {

            @Test
            @DisplayName("InvalidBookingParticipantsException을 발생시킨다.")
            void it_throws_InvalidBookingParticipantsException() {
                // Given
                String time = "저녁";
                String place = "탁구대";
                List<Long> participantIds = List.of(2L, 3L);
                Place placeEntity = Place.builder()
                        .placeName(place)
                        .maxCapacity(4)
                        .build();
                TimeSlotId timeSlotId = new TimeSlotId(1L, time);
                TimeSlot timeSlot = TimeSlot.builder()
                        .timeSlotId(timeSlotId)
                        .place(placeEntity)
                        .build();
                Member president = Member.builder()
                        .id(2L)
                        .name("성춘향")
                        .build();
                List<Member> participants = List.of(
                        Member.builder().id(2L).name("성춘향").build(),
                        Member.builder().id(3L).name("참가자1").build()
                );
                when(timeSlotPersistencePort.findAllTimeSlots()).thenReturn(List.of(timeSlot));
                when(bookingPersistencePort.findBookingByDateAndTimeAndPlace(any(), any(), any()))
                        .thenReturn(List.of());
                when(memberPersistencePort.findMemberById(2L)).thenReturn(president);
                when(memberPersistencePort.findMembersByIds(participantIds)).thenReturn(participants);

                // When & Then
                assertThrows(InvalidBookingParticipantsException.class, () ->
                        createBookingUseCase.execute(time, place, participantIds)
                );
            }
        }

        @Nested
        @DisplayName("유효하지 않은 TimeSlot 정보일 때")
        class Context_with_invalid_time_slot {

            @Test
            @DisplayName("InvalidBookingInfomationException을 발생시킨다.")
            void it_throws_InvalidBookingInfomationException() {
                // Given
                String time = "저녁";
                String place = "탁구대";
                List<Long> participantIds = List.of(3L, 4L);
                when(timeSlotPersistencePort.findAllTimeSlots()).thenReturn(List.of());

                // When & Then
                assertThrows(InvalidBookingInfomationException.class, () ->
                        createBookingUseCase.execute(time, place, participantIds)
                );
            }
        }
    }
}