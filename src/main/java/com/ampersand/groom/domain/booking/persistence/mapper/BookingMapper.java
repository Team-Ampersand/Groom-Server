package com.ampersand.groom.domain.booking.persistence.mapper;

import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.domain.TimeSlot;
import com.ampersand.groom.domain.booking.persistence.entity.BookingJpaEntity;
import com.ampersand.groom.domain.booking.presentation.data.response.GetBookingResponse;
import com.ampersand.groom.domain.member.domain.Member;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.domain.member.presentation.data.response.GetMemberResponse;
import com.ampersand.groom.global.entity.BaseIdEntity;
import com.ampersand.groom.global.mapper.GenericMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingMapper implements GenericMapper<BookingJpaEntity, Booking> {

    private final MemberMapper memberMapper;
    private final TimeSlotMapper timeSlotMapper;

    @Override
    public Booking toDomain(BookingJpaEntity bookingJpaEntity) {
        return Booking.builder()
                .id(bookingJpaEntity.getId())
                .president(memberMapper.toDomain(bookingJpaEntity.getPresident()))
                .participants(bookingJpaEntity.getParticipants().stream().sorted(Comparator.comparing(BaseIdEntity::getId)).map(memberMapper::toDomain).collect(Collectors.toList()))
                .timeSlot(timeSlotMapper.toDomain(bookingJpaEntity.getTimeSlot()))
                .bookingDate(bookingJpaEntity.getBookingDate())
                .build();
    }

    @Override
    public BookingJpaEntity toEntity(Booking booking) {
        return BookingJpaEntity.builder()
                .id(booking.getId())
                .president(memberMapper.toEntity(booking.getPresident()))
                .participants(booking.getParticipants().stream().map(memberMapper::toEntity).collect(Collectors.toSet()))
                .timeSlot(timeSlotMapper.toEntity(booking.getTimeSlot()))
                .bookingDate(booking.getBookingDate())
                .build();
    }

    public GetBookingResponse toResponse(Booking booking) {
        TimeSlot timeSlot = booking.getTimeSlot();
        Member president = booking.getPresident();
        return new GetBookingResponse(
                booking.getId(),
                timeSlot.getPlace().getPlaceName(),
                new GetMemberResponse(
                        president.getId(),
                        president.getEmail(),
                        president.getGeneration(),
                        president.getName(),
                        president.getIsAvailable(),
                        president.getRole()
                ),
                booking.getParticipants().stream()
                        .map(memberMapper::toResponse)
                        .toList(),
                booking.getBookingDate(),
                timeSlot.getTimeSlotId().timeLabel()
        );
    }
}