package com.mybank.atm.service;

import com.mybank.atm.entity.db.BankNote;
import com.mybank.atm.entity.db.Safe;
import com.mybank.atm.exception.ServiceException;
import com.mybank.atm.repo.SafeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class SafeServiceMockTest {

    @Spy
    private SafeService safeService;
    @Mock
    private Safe fiftySafe;
    @Mock
    private Safe twentySafe;
    @Mock
    private Safe tenSafe;
    @Mock
    private Safe fiveSafe;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        SafeRepository safeRepository = mock(SafeRepository.class);
        safeService.setSafeRepository(safeRepository);

        when(safeRepository.getByBankNote(BankNote.FIFTY)).thenReturn(fiftySafe);
        when(safeRepository.getByBankNote(BankNote.TWENTY)).thenReturn(twentySafe);
        when(safeRepository.getByBankNote(BankNote.TEN)).thenReturn(tenSafe);
        when(safeRepository.getByBankNote(BankNote.FIVE)).thenReturn(fiveSafe);
    }

    @Test
    public void dispenseTest_75() throws ServiceException {
        // Put some notes in the safe
        when(fiftySafe.getCount()).thenReturn(2);
        when(twentySafe.getCount()).thenReturn(1);
        when(fiveSafe.getCount()).thenReturn(1);
        Map<BankNote, Integer> expected = new HashMap<>();
        expected.put(BankNote.FIFTY, 1);
        expected.put(BankNote.TWENTY, 1);
        expected.put(BankNote.FIVE, 1);
        assertTrue("Incorrect number of notes returned", expected.equals(safeService.calculateNotes(75)));
    }

    @Test
    public void dispenseTest_45() throws ServiceException {
        // Put some notes in the safe
        when(fiveSafe.getCount()).thenReturn(10);
        Map<BankNote, Integer> expected = new HashMap<>();
        expected.put(BankNote.FIVE, 9);
        assertTrue("Incorrect number of notes returned", expected.equals(safeService.calculateNotes(45)));
    }

    @Test
    public void dispenseTest_20() throws ServiceException {
        // Put some notes in the safe
        when(tenSafe.getCount()).thenReturn(1);
        when(fiveSafe.getCount()).thenReturn(20);
        Map<BankNote, Integer> expected = new HashMap<>();
        expected.put(BankNote.TEN, 1);
        expected.put(BankNote.FIVE, 2);
        assertTrue("Incorrect number of notes returned", expected.equals(safeService.calculateNotes(20)));
    }

    @Test(expected = ServiceException.class)
    public void dispenseTest_insufficient() throws ServiceException {
        when(fiftySafe.getCount()).thenReturn(2);
        safeService.calculateNotes(105);
    }
}
