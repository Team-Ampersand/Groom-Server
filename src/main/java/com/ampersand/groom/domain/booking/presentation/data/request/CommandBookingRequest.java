package com.ampersand.groom.domain.booking.presentation.data.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CommandBookingRequest(
        @NotNull
        String time,
        @NotNull
        String place,
        @NotNull
        List<Long> participants
) {
}