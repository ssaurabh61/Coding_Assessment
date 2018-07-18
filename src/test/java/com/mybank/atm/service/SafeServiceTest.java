package com.mybank.atm.service;

import com.mybank.atm.entity.db.BankNote;
import com.mybank.atm.exception.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class SafeServiceTest {

    @TestConfiguration
    static class SafeServiceImplTestContextConfiguration {
        @Bean
        public SafeService safeService() {
            return new SafeService();
        }
    }

    @Autowired
    SafeService safeService;

    @Test
    public void getTotalTest() {
        int total = 2000;
        assertEquals("Invalid Total", (int) safeService.getTotal(), total);
    }

    @Test
    public void calculateNotesTest1() throws ServiceException {
        Map<BankNote, Integer> expectedMap = new HashMap<>();
        expectedMap.put(BankNote.FIFTY, 20);
        expectedMap.put(BankNote.TWENTY, 2);
        expectedMap.put(BankNote.FIVE, 1);
        Map<BankNote, Integer> actualMap = safeService.calculateNotes(1045);
        assertTrue(expectedMap.equals(actualMap));
    }

    @Test
    public void calculateNotesTest2() throws ServiceException {
        Map<BankNote, Integer> expectedMap = new HashMap<>();
        expectedMap.put(BankNote.TEN, 1);
        expectedMap.put(BankNote.FIVE, 1);
        Map<BankNote, Integer> actualMap = safeService.calculateNotes(15);
        assertTrue(expectedMap.equals(actualMap));
    }

    @Test(expected = ServiceException.class)
    public void calculateNotesInvalidTest1() throws ServiceException {
        safeService.calculateNotes(18);
    }

    @Test(expected = ServiceException.class)
    public void calculateNotesInvalidTest2() throws ServiceException {
        safeService.calculateNotes(40000000);
    }

    @Test
    public void testWithdrawCash() throws ServiceException {
        Map<BankNote, Integer> map = new HashMap<>();
        map.put(BankNote.FIFTY, 1);
        map.put(BankNote.TWENTY, 2);
        safeService.withdrawCash(map);
        assertEquals("Incorrect 50 count", 19, safeService.safeRepository.getByBankNote(BankNote.FIFTY).getCount());
        assertEquals("Incorrect 20 count", 28, safeService.safeRepository.getByBankNote(BankNote.TWENTY).getCount());
    }
}
