package com.ampersand.groom.domain.member.application.usecase;

import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.domain.Place;
import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.domain.constant.TimeSlotId;
import com.ampersand.groom.domain.member.application.port.MemberPersistencePort;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.presentation.data.response.GetCurrentMemberResponse;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindCurrentMemberUseCase 클래스의")
class FindCurrentMemberUseCaseTest {

    @Mock
    private MemberPersistencePort memberPersistencePort;

    @Mock
    private BookingPersistencePort bookingPersistencePort;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private FindCurrentMemberUseCase findCurrentMemberUseCase;

    private void mockSecurityContext(String email) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(email);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Nested
    @DisplayName("execute 메서드는")
    class Describe_execute {

        @Nested
        @DisplayName("현재 로그인한 회원이 존재할 때")
        class Context_with_logged_in_member {

            @Test
            @DisplayName("회원 정보와 예약 정보를 포함한 응답을 반환한다.")
            void it_returns_current_member_info_with_bookings() {
                // Given
                String email = "s00001@gsm.hs.kr";
                mockSecurityContext(email);
                Member currentMember = Member.builder()
                        .id(1L)
                        .name("홍길동")
                        .email(email)
                        .generation(13)
                        .role(MemberRole.ROLE_STUDENT)
                        .isAvailable(true)
                        .build();
                Place place = Place.builder().placeName("탁구대").build();
                TimeSlotId timeSlotId = new TimeSlotId(1L, "오후");
                TimeSlot timeSlot = TimeSlot.builder().timeSlotId(timeSlotId).place(place).build();
                Member participant1 = Member.builder()
                        .id(2L)
                        .name("성춘향")
                        .generation(14)
                        .email("s00002@gsm.hs.kr")
                        .role(MemberRole.ROLE_STUDENT)
                        .isAvailable(true)
                        .build();
                Member participant2 = Member.builder()
                        .id(3L)
                        .name("이몽룡")
                        .generation(15)
                        .email("s00003@gsm.hs.kr")
                        .role(MemberRole.ROLE_STUDENT)
                        .isAvailable(true)
                        .build();
                Booking booking = Booking.builder()
                        .timeSlot(timeSlot)
                        .bookingDate(LocalDate.of(2024, 3, 30))
                        .participants(List.of(participant1, participant2))
                        .build();
                when(memberPersistencePort.findMemberByEmail(email)).thenReturn(currentMember);
                when(bookingPersistencePort.findBookingByMemberId(currentMember.getId())).thenReturn(List.of(booking));
                when(memberMapper.toResponse(participant1)).thenReturn(new GetMemberResponse(participant1.getId(), participant1.getName(), participant1.getGeneration(), participant1.getEmail(), participant1.getIsAvailable(), participant1.getRole()));
                when(memberMapper.toResponse(participant2)).thenReturn(new GetMemberResponse(participant2.getId(), participant2.getName(), participant2.getGeneration(), participant2.getEmail(), participant2.getIsAvailable(), participant2.getRole()));

                // When
                GetCurrentMemberResponse response = findCurrentMemberUseCase.execute();

                // Then
                assertAll(
                        () -> assertEquals(currentMember.getId(), response.id()),
                        () -> assertEquals(currentMember.getName(), response.name()),
                        () -> assertEquals(currentMember.getEmail(), response.email()),
                        () -> assertEquals(currentMember.getRole(), response.role()),
                        () -> assertEquals(1, response.booked().size()),
                        () -> assertEquals("탁구대", response.booked().getFirst().place()),
                        () -> assertEquals("오후", response.booked().getFirst().time()),
                        () -> assertEquals(LocalDate.of(2024, 3, 30), response.booked().getFirst().date()),
                        () -> assertEquals(2, response.booked().getFirst().participants().size())
                );
            }
        }
    }
}