package com.mybank.atm.exception;

/**
 * General exception container.
 *
 * @author brian.e.reynolds@outlook.com
 */
public class ServiceException extends Exception {
    private final String code;
    private final String message;

    public ServiceException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
