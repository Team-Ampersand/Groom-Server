package com.ampersand.groom.domain.auth.expection;

import com.ampersand.groom.global.error.ErrorCode;
import com.ampersand.groom.global.error.exception.GroomException;

public class EmailOrPasswordEmptyException extends GroomException {
    public EmailOrPasswordEmptyException() {
        super(ErrorCode.EMAIL_OR_PASSWORD_EMPTY);
    }
}
