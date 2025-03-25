package com.ampersand.groom.domain.booking.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class InvalidBookingParticipantsException extends GroomException {
    public InvalidBookingParticipantsException() {
        super(ErrorCode.INVALID_BOOKING_PARTICIPANTS);
    }
}