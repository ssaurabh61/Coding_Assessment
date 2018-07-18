package com.mybank.atm.entity.db;

/**
 * Entity to represent a Bank Note
 *
 * @author brian.e.reynolds@outlook.com
 */
public enum BankNote {

    FIFTY(50), TWENTY(20), TEN(10), FIVE(5);

    private int value;

    BankNote(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
