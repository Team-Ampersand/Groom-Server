package com.ampersand.groom.infrastructure.scheduler;

import com.ampersand.groom.domain.booking.application.port.BookingApplicationPort;
import com.ampersand.groom.domain.booking.application.port.BookingPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingDeletionScheduler {

    private final BookingApplicationPort bookingApplicationPort;

    @Scheduled(cron = "30 20 3 * * *")
    public void deleteExpiredBookings() {
        bookingApplicationPort.deleteOldBookings();
    }
}