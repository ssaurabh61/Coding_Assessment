package com.mybank.atm.controller;

import com.mybank.atm.config.ErrorCodes;
import com.mybank.atm.config.ErrorMessages;
import com.mybank.atm.entity.db.Account;
import com.mybank.atm.entity.db.BankNote;
import com.mybank.atm.entity.json.AccountResource;
import com.mybank.atm.entity.json.CashMapResource;
import com.mybank.atm.entity.json.ErrorResource;
import com.mybank.atm.entity.json.WithdrawalResource;
import com.mybank.atm.exception.ApiException;
import com.mybank.atm.exception.ServiceException;
import com.mybank.atm.service.AccountService;
import com.mybank.atm.service.PinService;
import com.mybank.atm.service.SafeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * RESTful controller for the ATM
 *
 * @author brian.e.reynolds@outlook.com
 */
@RestController
@Api("API to implement ATM functionality for getting current balance, and withdrawing cash")
public class AtmController {

    private Logger logger = LoggerFactory.getLogger(AtmController.class);

    @Autowired
    private PinService pinService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SafeService safeService;

    /**
     * Allows the user to request a balance check.
     * <ul>
     * <li>The current balance and maximum withdrawal amount will be shown</li>
     * <li>The customer PIN and Account number must be valid</li>
     * </ul>
     *
     * @param accountNum The customer account number
     * @param pin        The customer secret PIN
     * @return Balance information; the current balance, and maximum withdrawal amount
     * @throws ApiException {@link ErrorCodes#PIN_VALIDATION} {@link ErrorMessages#INVALID_PIN}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/balance/{accountNum}/{pin}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Get Balance and Max Withdrawal information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = AccountResource.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorResource.class)
    })
    @ResponseBody
    public AccountResource getBalance(@PathVariable Long accountNum, @PathVariable String pin)
            throws ApiException {
        AccountResource accountResource = new AccountResource();

        try {
            if (!pinService.validatePin(accountNum, pin)) {
                throw new ServiceException(ErrorCodes.PIN_VALIDATION, ErrorMessages.INVALID_PIN);
            }

            Account account = accountService.getAccount(accountNum);
            BigDecimal accountBalance = account.getBalance();
            accountResource.setBalance(accountBalance);

            // Consideration: We could also possibly limit the max withdrawal by the amount of
            // cash that is left in the machine; but this would be a security lapse

            accountResource.setMaxWithdrawal(account.getBalance().add(account.getOverdraft()));
        } catch (ServiceException se) {
            logger.error("getBalance failed for {}", accountNum);
            throw new ApiException(se);
        }

        return accountResource;
    }

    /**
     * Withdraw funds from the ATM
     * <ul>
     * <li>The ATM has denominations of 50, 20, 10 and 5</li>
     * <li>The customer PIN and Account number must be valid</li>
     * <li>The ATM will dispense the minimum amount of notes</li>
     * <li>The amount dispensed is limited by either the customer balance or physical number of notes</li>
     * </ul>
     *
     * @param accountNum CThe customer account number
     * @param pin        The customer secret PIN
     * @param amount     The amount requested
     * @return Withdrawal information, the amount withdrawn, the notes withdrawn, and updated account balance information.
     * @throws ApiException {@link ErrorCodes#PIN_VALIDATION} {@link ErrorMessages#INVALID_PIN}
     * @throws ApiException {@link ErrorCodes#ACCOUNT_FUNDS} {@link ErrorMessages#ACCOUNT_FUNDS_INSUFFICIENT}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/withdraw/{accountNum}/{pin}/{amount}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Withdraw funds from the ATM")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = AccountResource.class),
            @ApiResponse(code = 400, message = "Error", response = ErrorResource.class)
    })
    @Transactional(rollbackFor = ApiException.class)
    public WithdrawalResource withdraw(@PathVariable Long accountNum, @PathVariable String pin, @PathVariable Integer amount)
            throws ApiException {
        WithdrawalResource withdrawalResource = new WithdrawalResource();

        try {
            if (!pinService.validatePin(accountNum, pin)) {
                throw new ServiceException(ErrorCodes.PIN_VALIDATION, ErrorMessages.INVALID_PIN);
            }

            // Check that the customer has enough credit in their a/c
            Account account = accountService.getAccount(accountNum);
            BigDecimal totalFunds = account.getBalance().add(account.getOverdraft());
            if (totalFunds.compareTo(BigDecimal.valueOf(amount)) < 0) {
                throw new ServiceException(ErrorCodes.ACCOUNT_FUNDS, ErrorMessages.ACCOUNT_FUNDS_INSUFFICIENT);
            }

            // Withdraw funds from customer account and the ATM safe
            Map<BankNote, Integer> cashMap = safeService.calculateNotes(amount);
            accountService.withdrawFunds(accountNum, BigDecimal.valueOf(amount));
            safeService.withdrawCash(cashMap);
            withdrawalResource.setCashMap(new CashMapResource(cashMap));

            // Display updated information to user
            Account updatedAccount = accountService.getAccount(accountNum);
            withdrawalResource.setAccountResource(new AccountResource(
                    updatedAccount.getBalance(),
                    updatedAccount.getBalance().add(updatedAccount.getOverdraft())
            ));

        } catch (ServiceException se) {
            logger.error("withdrawal failed for {}", accountNum);
            throw new ApiException(se);
        }

        withdrawalResource.setAmount(BigDecimal.valueOf(amount));

        return withdrawalResource;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiException.class)
    public ErrorResource handleApiException(ApiException e) {
        logger.error("handleApiException: code: {}, message: {}", e.getCode(), e.getMessage());
        logger.trace("handleApiException: stacktrace: {}", ExceptionUtils.getStackTrace(e));
        return new ErrorResource(e.getCode(), e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResource handleGeneralException(Throwable e) {
        logger.error("handleGeneralException: message: {}", e.getMessage());
        logger.error("handleGeneralException: stacktrace: {}", ExceptionUtils.getStackTrace(e));
        return new ErrorResource(ErrorCodes.SYSTEM_ERROR, e.getMessage());

    }
}
