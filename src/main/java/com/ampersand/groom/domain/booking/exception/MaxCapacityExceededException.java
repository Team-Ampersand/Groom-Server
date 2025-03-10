package com.ampersand.groom.domain.booking.exception;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class MaxCapacityExceededException extends GroomException {
    public MaxCapacityExceededException() {
        super(ErrorCode.MAX_CAPACITY_EXCEEDED);
    }
}