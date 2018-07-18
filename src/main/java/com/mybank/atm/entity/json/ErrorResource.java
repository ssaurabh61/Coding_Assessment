package com.mybank.atm.entity.json;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * JSON Resource entity used to display errors to user
 *
 * @author brian.e.reynolds@outlook.com
 */
@JsonRootName(value = "Error")
public class ErrorResource {
    private String code;
    private String message;

    public ErrorResource(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
