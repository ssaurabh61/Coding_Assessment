package com.mybank.atm.config;

/**
 * Helper utilities
 *
 * @author brian.e.reynolds@outlook.com
 */
public class Utils {
    private Utils() {
        // Hide implicit constructor
    }

    /**
     * Mask sensitive string data for the purposes of logging, etc.
     *
     * @param str The string to mask
     * @return The masked string
     */
    @SuppressWarnings("squid:S2639") // I know what I'm doing with the regex, thanks
    public static String maskString(String str) {
        return str.isEmpty() ? str : str.replaceAll(".", "\\*");
    }
}
