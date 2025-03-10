package com.ampersand.groom.domain.booking.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class NotBookingDateException extends GroomException {
    public NotBookingDateException() {
        super(ErrorCode.NOT_BOOKING_DATE);
    }
}