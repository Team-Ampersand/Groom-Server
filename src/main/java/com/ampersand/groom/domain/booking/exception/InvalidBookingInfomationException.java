package com.ampersand.groom.domain.booking.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class InvalidBookingInfomationException extends GroomException {
    public InvalidBookingInfomationException() {
        super(ErrorCode.INVALID_BOOKING_INFORMATION);
    }
}