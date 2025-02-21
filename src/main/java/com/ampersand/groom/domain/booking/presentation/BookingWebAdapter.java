package com.ampersand.groom.domain.booking.presentation;

import com.ampersand.groom.domain.booking.application.port.BookingApplicationPort;
import com.ampersand.groom.domain.booking.presentation.data.response.GetPlaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1/booking")
@RestController
@RequiredArgsConstructor
public class BookingWebAdapter {

    private final BookingApplicationPort bookingApplicationPort;

    @GetMapping("/available")
    public ResponseEntity<List<GetPlaceResponse>> getAvailableBookings(
            @RequestParam(value = "date") LocalDate date,
            @RequestParam(value = "time") String time,
            @RequestParam(value = "placeType",required = false) String placeType
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingApplicationPort.findPlaceByBookingAvailability(date, time, placeType));
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
