package com.mybank.atm.config;

/**
 * Error Messages that will be returned to the user
 *
 * @author brian.e.reynolds@outlook.com
 */
public class ErrorMessages {

    private ErrorMessages() {
        // Hide implicit constructor
    }

    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String ACCOUNT_FUNDS_INSUFFICIENT = "Insufficient account balance";
    public static final String INVALID_PIN = "Invalid PIN provided";
    public static final String ATM_FUNDS_WITHDRAWAL_ERROR = "Insufficient ATM funds";
    public static final String ATM_FUNDS_AMOUNT_ERROR = "Cannot dispense this amount";
    public static final String ATM_NOTES_WITHDRAWAL_ERROR = "Insufficient ATM notes to complete withdrawal";
}
