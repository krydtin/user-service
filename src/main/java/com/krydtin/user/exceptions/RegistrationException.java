package com.krydtin.user.exceptions;

import com.krydtin.user.constant.ErrorCode;

public class RegistrationException extends Exception {

    private final ErrorCode.Registration errorCode;
    private final String message;

    public RegistrationException(ErrorCode.Registration errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode.Registration getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
