package com.mybank.atm.entity.json;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.mybank.atm.entity.db.BankNote;

import java.util.Map;

/**
 * JSON Resource entity to represent a count of bank notes
 *
 * @author brian.e.reynolds@outlook.com
 */
@JsonRootName(value = "CashMap")
public class CashMapResource {

    private int fifty;
    private int twenty;
    private int ten;
    private int five;

    public CashMapResource(Map<BankNote, Integer> map) {
        for (Map.Entry<BankNote, Integer> entry : map.entrySet()) {
            if (entry.getKey() == BankNote.FIFTY) {
                fifty = entry.getValue();
            } else if (entry.getKey() == BankNote.TWENTY) {
                twenty = entry.getValue();
            } else if (entry.getKey() == BankNote.TEN) {
                ten = entry.getValue();
            } else if (entry.getKey() == BankNote.FIVE) {
                five = entry.getValue();
            }
        }
    }

    public int getFifty() {
        return fifty;
    }

    public int getTwenty() {
        return twenty;
    }

    public int getTen() {
        return ten;
    }

    public int getFive() {
        return five;
    }
}
