package com.mybank.atm.service;

import com.mybank.atm.entity.db.Account;
import com.mybank.atm.exception.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static com.mybank.atm.config.TestMessageConstants.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountServiceTest {
    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration {
        @Bean
        public AccountService accountService() {
            return new AccountService();
        }
    }

    @Autowired
    private AccountService accountService;

    // from src/test/resources/data.sql
    private Long testAccountNum = 987654321L;
    private String testPin = "4321";
    private BigDecimal testBalance = BigDecimal.valueOf(1230.00);
    private BigDecimal testOverdraft = BigDecimal.valueOf(150.00);

    private Long invalidAccountNum = 88888L;

    @Test
    public void testGetTotalFunds() throws ServiceException {
        BigDecimal calculatedFunds = testBalance.add(testOverdraft);
        assertTrue("Total funds are incorrect", accountService.getTotalFunds(testAccountNum).compareTo(calculatedFunds) == 0);
    }

    @Test
    public void testGetAccount() throws ServiceException {
        Account account = accountService.getAccount(testAccountNum);
        assertTrue(MSG_BAD_ACCOUNT_NUMBER, account.getAccountNumber().equals(testAccountNum));
        assertTrue(MSG_BAD_PIN, account.getPin().equals(testPin));
        assertTrue(MSG_BAD_BALANCE, account.getBalance().compareTo(testBalance) == 0);
        assertTrue(MSG_BAD_OVERDRAFT, account.getOverdraft().compareTo(testOverdraft) == 0);
    }

    @Test(expected = ServiceException.class)
    public void testInvalidAccount() throws ServiceException {
        accountService.getAccount(invalidAccountNum);
    }

    @Test
    public void testWithdrawFunds() throws ServiceException {
        final Long accountNum = 998877660L;
        final BigDecimal startingBalance = BigDecimal.valueOf(484.55);
        final BigDecimal withDrawAmount = BigDecimal.valueOf(19.99);
        accountService.withdrawFunds(accountNum, withDrawAmount);

        Account account = accountService.getAccount(accountNum);
        assertEquals("Balance is not correct", startingBalance.subtract(withDrawAmount), account.getBalance());
    }

    @Test
    public void testWithdrawFundsOverdraft() throws ServiceException {
        final Long accountNum = 494911101L;
        final BigDecimal withDrawAmount = BigDecimal.valueOf(200);
        accountService.withdrawFunds(accountNum, withDrawAmount);

        Account account = accountService.getAccount(accountNum);
        assertEquals("Balance is not correct", BigDecimal.ZERO, account.getBalance());
        assertTrue("Overdraft is not correct", BigDecimal.valueOf(300L).compareTo(account.getOverdraft()) == 0);
    }
}
