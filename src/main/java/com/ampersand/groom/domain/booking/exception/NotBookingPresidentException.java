package com.ampersand.groom.domain.booking.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class NotBookingPresidentException extends GroomException {
    public NotBookingPresidentException() {
        super(ErrorCode.NOT_BOOKING_PRESIDENT);
    }
}