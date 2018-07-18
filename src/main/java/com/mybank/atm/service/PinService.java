package com.mybank.atm.service;

import com.mybank.atm.config.ErrorCodes;
import com.mybank.atm.config.ErrorMessages;
import com.mybank.atm.config.Utils;
import com.mybank.atm.entity.db.Account;
import com.mybank.atm.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer logic for working with secret PINs
 *
 * @author brian.e.reynolds@outlook.com
 */
@Service
public class PinService {
    private Logger logger = LoggerFactory.getLogger(PinService.class);

    @Autowired
    AccountService accountService;

    /**
     * Validate the secret PIN
     *
     * @param accountNum The customer account number
     * @param pin        The customer secret PIN
     * @return Valid PIN (true), Invalid PIN (false)
     * @throws ServiceException {@link ErrorCodes#INVALID_PIN} {@link ErrorMessages#INVALID_PIN}
     */
    public boolean validatePin(Long accountNum, String pin) throws ServiceException {
        if (pin.isEmpty()) {
            logger.error("validatePin: Empty PIN");
            throw new ServiceException(ErrorCodes.INVALID_PIN, ErrorMessages.INVALID_PIN);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("validatePin: accountNum: {}, pin: {}", accountNum, Utils.maskString(pin));
        }
        Account account = accountService.getAccount(accountNum);
        return account.getPin().equals(pin);
    }
}
