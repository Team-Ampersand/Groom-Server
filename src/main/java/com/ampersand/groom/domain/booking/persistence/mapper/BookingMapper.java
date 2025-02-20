package com.ampersand.groom.domain.booking.persistence.mapper;

import com.ampersand.groom.domain.booking.domain.Booking;
import com.ampersand.groom.domain.booking.persistence.entity.BookingJpaEntity;
import com.ampersand.groom.domain.member.persistence.mapper.MemberMapper;
import com.ampersand.groom.global.mapper.GenericMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
                .participants(bookingJpaEntity.getParticipants().stream().map(memberMapper::toDomain).collect(Collectors.toList()))
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
}