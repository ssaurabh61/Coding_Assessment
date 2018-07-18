package com.mybank.atm.repo;

import com.mybank.atm.entity.db.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void getAllAccountsTest() {
        assertTrue("No accounts loaded", accountRepository.count() > 0);
    }

    @Test
    public void addNewAccountTest() {
        Long testAccountNumber = 399944352L;
        String testPin = "4949";
        BigDecimal testBalance = BigDecimal.valueOf(100);
        BigDecimal testOverdraft = BigDecimal.valueOf(50);
        Account newAccount = new Account(testAccountNumber, testPin);
        newAccount.setBalance(testBalance);
        newAccount.setOverdraft(testOverdraft);
        accountRepository.save(newAccount);

        List<Account> accounts = accountRepository.findByAccountNumber(testAccountNumber);
        assertEquals("Incorrect number of accounts returned", 1, accounts.size());

        Account account = accounts.get(0);
        assertEquals("Account number incorrect", account.getAccountNumber(), testAccountNumber);
        assertEquals("Balance Incorrect", account.getBalance(), testBalance);
        assertEquals("Overdraft incorrect", account.getOverdraft(), testOverdraft);
        assertEquals("PIN Incorrect", account.getPin(), testPin);
        assertTrue("Invalid ID", account.getId() > 0);
    }

    @Test
    public void getAccountTest() {
        // From src/test/resources/data.sql
        Long testAccountNumber = 998877660L;
        String testPin = "0011";
        BigDecimal testBalance = BigDecimal.valueOf(484.55);
        BigDecimal testOverdraft = BigDecimal.valueOf(199.99);

        List<Account> accounts = accountRepository.findByAccountNumber(testAccountNumber);
        assertEquals("Incorrect number of accounts returned", 1, accounts.size());

        Account account = accounts.get(0);
        assertEquals("Account number incorrect", account.getAccountNumber(), testAccountNumber);
        assertEquals("Balance Incorrect", account.getBalance(), testBalance);
        assertEquals("Overdraft incorrect", account.getOverdraft(), testOverdraft);
        assertEquals("PIN Incorrect", account.getPin(), testPin);
        assertTrue("Invalid ID", account.getId() > 0);
    }
}
