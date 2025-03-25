package com.ampersand.groom.domain.booking.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class DuplicateBookingException extends GroomException{
    public DuplicateBookingException(){
        super(ErrorCode.DUPLICATE_BOOKING);
    }
}