package com.ampersand.groom.domain.booking.persistence.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookingJpaRepositoryCustomImpl {

    private final JPAQueryFactory queryFactory;
}
