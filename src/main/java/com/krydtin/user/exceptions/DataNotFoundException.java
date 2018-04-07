package com.krydtin.user.exceptions;

import com.krydtin.user.constant.ErrorCode;

public class DataNotFoundException extends Exception{
private final ErrorCode.DataNotFound errorCode;
    private final String message;

    public DataNotFoundException(ErrorCode.DataNotFound errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode.DataNotFound getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
