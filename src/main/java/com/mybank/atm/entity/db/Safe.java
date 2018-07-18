package com.mybank.atm.entity.db;

import javax.persistence.*;

/**
 * Entity to represent the Safe containing the bank notes
 *
 * @author brian.e.reynolds@outlook.com
 */
@Entity
public class Safe {

    @Id
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BankNote bankNote;

    @Column(nullable = false, name = "note_count")
    private int count;

    public BankNote getBankNote() {
        return bankNote;
    }

    public void setBankNote(BankNote bankNote) {
        this.bankNote = bankNote;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
