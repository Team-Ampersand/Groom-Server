package com.ampersand.groom.domain.booking.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class BookingNotFoundException extends GroomException {
    public BookingNotFoundException() {
        super(ErrorCode.BOOKING_NOT_FOUND);
    }
}