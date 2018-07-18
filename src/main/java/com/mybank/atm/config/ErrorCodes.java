package com.mybank.atm.config;

/**
 * Various error codes that will be used when throwing exceptions
 *
 * @author brian.e.reynolds@outlook.com
 */
public class ErrorCodes {

    private ErrorCodes() {
        // Hide implicit constructor
    }

    public static final String PIN_VALIDATION = "P1001";
    public static final String INVALID_PIN = "P1002";
    public static final String ACCOUNT_LOOKUP = "A1001";
    public static final String ACCOUNT_FUNDS = "A1002";
    public static final String ATM_FUNDS_ERROR = "S1001";
    public static final String SYSTEM_ERROR = "Y1001";
}
