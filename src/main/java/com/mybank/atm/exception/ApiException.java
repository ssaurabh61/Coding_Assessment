package com.mybank.atm.exception;

/**
 * Wrapper class. Used to catch/display exceptions that
 * occur in the upper-levels of the stack
 *
 * @author brian.e.reynolds@outlook.com
 */
public class ApiException extends Exception {
    private final String code;
    private final String message;

    public ApiException(ServiceException se) {
        this.code = se.getCode();
        this.message = se.getMessage();
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
