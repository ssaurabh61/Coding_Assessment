package com.mybank.atm.entity.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
public class SafeTest {

    private static final String MSG_NOTE_ERROR = "Note does not match";
    private static final String MSG_COUNT_ERROR = "Count does not match";

    @Test
    public void testSafeFifty() {
        Safe safeFifty = new Safe();
        safeFifty.setBankNote(BankNote.FIFTY);
        safeFifty.setCount(12);
        assertTrue(MSG_NOTE_ERROR, safeFifty.getBankNote().equals(BankNote.FIFTY));
        assertTrue(MSG_COUNT_ERROR, safeFifty.getCount() == 12);
    }

    @Test
    public void testSafeTwenty() {
        Safe safeTwenty = new Safe();
        safeTwenty.setBankNote(BankNote.TWENTY);
        safeTwenty.setCount(17);
        assertTrue(MSG_NOTE_ERROR, safeTwenty.getBankNote().equals(BankNote.TWENTY));
        assertTrue(MSG_COUNT_ERROR, safeTwenty.getCount() == 17);
    }

    @Test
    public void testSafeTen() {
        Safe safeTen = new Safe();
        safeTen.setBankNote(BankNote.TEN);
        safeTen.setCount(2);
        assertTrue(MSG_NOTE_ERROR, safeTen.getBankNote().equals(BankNote.TEN));
        assertTrue(MSG_COUNT_ERROR, safeTen.getCount() == 2);
    }

    @Test
    public void testSafeFive() {
        Safe safeFive = new Safe();
        safeFive.setBankNote(BankNote.FIVE);
        safeFive.setCount(33);
        assertTrue(MSG_NOTE_ERROR, safeFive.getBankNote().equals(BankNote.FIVE));
        assertTrue(MSG_COUNT_ERROR, safeFive.getCount() == 33);
    }
}
