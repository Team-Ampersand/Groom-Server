package com.ampersand.groom.domain.booking.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class InvalidBookingTimeException extends GroomException {
    public InvalidBookingTimeException() {
        super(ErrorCode.INVALID_BOOKING_TIME);
    }
}