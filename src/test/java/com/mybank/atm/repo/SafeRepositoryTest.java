package com.mybank.atm.repo;

import com.mybank.atm.entity.db.BankNote;
import com.mybank.atm.entity.db.Safe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class SafeRepositoryTest {

    private static final String MSG_INVALID_50_NOTES = "Invalid number of 50 notes";
    private static final String MSG_INVALID_20_NOTES = "Invalid number of 20 notes";
    private static final String MSG_INVALID_10_NOTES = "Invalid number of 10 notes";
    private static final String MSG_INVALID_5_NOTES = "Invalid number of 5 notes";

    @Autowired
    private SafeRepository safeRepository;

    @Test
    public void testGetNotes() {
        Safe safeFifty = safeRepository.getByBankNote(BankNote.FIFTY);
        assertEquals(MSG_INVALID_50_NOTES, 20, safeFifty.getCount());

        Safe safeTwenty = safeRepository.getByBankNote(BankNote.TWENTY);
        assertEquals(MSG_INVALID_20_NOTES, 30, safeTwenty.getCount());

        Safe safeTenner = safeRepository.getByBankNote(BankNote.TWENTY);
        assertEquals(MSG_INVALID_10_NOTES, 30, safeTenner.getCount());

        Safe safeFiver = safeRepository.getByBankNote(BankNote.FIVE);
        assertEquals(MSG_INVALID_5_NOTES, 20, safeFiver.getCount());
    }


}
