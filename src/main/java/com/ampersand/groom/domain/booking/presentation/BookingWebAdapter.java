package com.ampersand.groom.domain.booking.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/bookings")
@RestController
@RequiredArgsConstructor
public class BookingWebAdapter {

    // 필요한 의존성을 명시하여 주세요

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableBookings() {
        return null;  // TODO: 구현
    }

    @GetMapping("/information")
    public ResponseEntity<?> getBookingInformation() {
        return null;  // TODO: 구현
    }

    @PostMapping
    public ResponseEntity<?> createBooking() {
        return null;  // TODO: 구현
    }

    @PatchMapping
    public ResponseEntity<?> updateBooking() {
        return null;  // TODO: 구현
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking() { // TODO: 구현
    }
}
