package com.krydtin.user.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.krydtin.user.constant.ErrorCode;
import com.krydtin.user.model.ErrorMessage;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler({DataNotFoundException.class})
    public ErrorMessage handleBadRequest(HttpServletRequest request, DataNotFoundException e) {
        return ErrorMessage.builder()
                .code(e.getErrorCode().getCode())
                .message(e.getMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class})
    public ErrorMessage handleBadRequest(HttpServletRequest request, AuthenticationException e) {
        return ErrorMessage.builder()
                .code(e.getErrorCode().getCode())
                .message(e.getMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RegistrationException.class)
    public ErrorMessage exceptionHandler(HttpServletRequest request, RegistrationException e) {
        return ErrorMessage.builder()
                .code(e.getErrorCode().getCode())
                .message(e.getMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorMessage<Error> handleMethodArgumentNotValidExceptionError(MethodArgumentNotValidException e) {
        final BindingResult result = e.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        final ErrorMessage<Error> errorResponse = new ErrorMessage<>();
        errorResponse.setCode(ErrorCode.Validation.VALIDATION_ERROR.getCode());
        errorResponse.setMessage(ErrorCode.Validation.VALIDATION_ERROR.name());
        errorResponse.setData(processFieldErrors(fieldErrors));
        return errorResponse;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage<String> handleHttpMessageNotReadableException(Exception e) {
        ErrorMessage<String> result = new ErrorMessage<>();
        result.setCode("" + BAD_REQUEST.value());
        result.setMessage(e.getMessage());
        return result;
    }

    private Error processFieldErrors(List<FieldError> fieldErrors) {
        final Error error = new Error();
        fieldErrors.forEach(f -> {
            error.addFieldError(f.getField(), f.getDefaultMessage());
        });
        return error;
    }

    public class Error {

        @JsonIgnore
        private String description;

        private final List<Field> fieldErrors = new ArrayList<>();

        public void addFieldError(String name, String message) {
            final Field error = new Field(name, message);
            fieldErrors.add(error);
        }

        public List<Field> getFieldErrors() {
            return fieldErrors;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class Field {

        private final String name;
        private final String message;

        public Field(String name, String message) {
            this.name = name;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
    }

}
