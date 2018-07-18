package com.mybank.atm.service;

import com.mybank.atm.config.ErrorCodes;
import com.mybank.atm.config.ErrorMessages;
import com.mybank.atm.entity.db.Account;
import com.mybank.atm.exception.ServiceException;
import com.mybank.atm.repo.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer logic for working with Accounts
 *
 * @author brian.e.reynolds@outlook.com
 */
@Service
public class AccountService {
    private Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    AccountRepository accountRepository;

    /**
     * Get the total funds that this account has (balance + overdraft)
     *
     * @param accountNum The customer account number
     * @return The total funds
     * @throws ServiceException {@link ErrorCodes#ACCOUNT_LOOKUP} {@link ErrorMessages#ACCOUNT_NOT_FOUND}
     */
    public BigDecimal getTotalFunds(Long accountNum) throws ServiceException {
        logger.debug("getTotalFunds: accountNum: {}", accountNum);
        Account account = getAccount(accountNum);
        return account.getBalance().add(account.getOverdraft());
    }

    /**
     * Get the Account object for this account number
     *
     * @param accountNum The customer account number
     * @return The Account object
     * @throws ServiceException {@link ErrorCodes#ACCOUNT_LOOKUP} {@link ErrorMessages#ACCOUNT_NOT_FOUND}
     */
    public Account getAccount(Long accountNum) throws ServiceException {
        logger.debug("getAccount: accountNum: {}", accountNum);
        List<Account> accounts = accountRepository.findByAccountNumber(accountNum);
        if (accounts.isEmpty()) {
            throw new ServiceException(ErrorCodes.ACCOUNT_LOOKUP, ErrorMessages.ACCOUNT_NOT_FOUND);
        } else if (accounts.size() > 1) {
            logger.warn("getAccount: duplicate accounts found: {}", accounts.size());
        }
        return accounts.get(0);
    }

    /**
     * Withdraw funds from this account
     *
     * @param accountNum The customer account number
     * @param amount     The amount to withdraw
     * @throws ServiceException {@link ErrorCodes#ACCOUNT_FUNDS} {@link ErrorMessages#ACCOUNT_FUNDS_INSUFFICIENT}
     * @throws ServiceException {@link ErrorCodes#ACCOUNT_LOOKUP} {@link ErrorMessages#ACCOUNT_NOT_FOUND}
     */
    public void withdrawFunds(Long accountNum, BigDecimal amount) throws ServiceException {
        logger.debug("withdrawFunds: accountNum: {}, amount: {}", accountNum, amount);

        Account account = getAccount(accountNum);

        if (account.getBalance().add(account.getOverdraft()).compareTo(amount) < 0) {
            throw new ServiceException(ErrorCodes.ACCOUNT_FUNDS, ErrorMessages.ACCOUNT_FUNDS_INSUFFICIENT);
        }

        if (account.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            // Into the overdraft
            BigDecimal creditAmount = amount.subtract(account.getBalance());
            account.setBalance(BigDecimal.ZERO);
            account.setOverdraft(account.getOverdraft().subtract(creditAmount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }

        accountRepository.save(account);
    }

}
