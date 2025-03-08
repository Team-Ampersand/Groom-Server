package com.ampersand.groom.domain.booking.presentation;

import com.ampersand.groom.domain.booking.application.port.BookingApplicationPort;
import com.ampersand.groom.domain.booking.presentation.data.response.GetBookingResponse;
import com.ampersand.groom.domain.booking.presentation.data.response.GetPlaceResponse;
import com.ampersand.groom.domain.booking.presentation.data.request.CommandBookingRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<GetBookingResponse>> getBookingInformation(
            @RequestParam(value = "date") LocalDate date,
            @RequestParam(value = "time") String time,
            @RequestParam(value = "placeType", required = false) String placeType
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingApplicationPort.findBookingByDateAndTimeAndPlace(date, time, placeType));
    }

    @PostMapping
    public ResponseEntity<Void> createBooking(@Valid @RequestBody CommandBookingRequest request) {
        bookingApplicationPort.createBooking(request.time(), request.place(), request.participants());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<?> updateBooking(
            @PathVariable(value = "bookingId") Long bookingId,
            @Valid @RequestBody CommandBookingRequest request
    ) {
        bookingApplicationPort.updateBooking(bookingId, request.time(), request.place(), request.participants());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBooking(@PathVariable(value = "bookingId") Long bookingId) {
        bookingApplicationPort.deleteBooking(bookingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
