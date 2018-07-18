package com.mybank.atm.service;

import com.mybank.atm.config.ErrorCodes;
import com.mybank.atm.config.ErrorMessages;
import com.mybank.atm.entity.db.BankNote;
import com.mybank.atm.entity.db.Safe;
import com.mybank.atm.exception.ServiceException;
import com.mybank.atm.repo.SafeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

/**
 * Service layer logic for working with ATM Safe
 *
 * @author brian.e.reynolds@outlook.com
 */
@Service
public class SafeService {

    private Logger logger = LoggerFactory.getLogger(SafeService.class);

    private BankNote[] allNotes = {BankNote.FIFTY, BankNote.TWENTY, BankNote.TEN, BankNote.FIVE};

    @Autowired
    SafeRepository safeRepository;

    /**
     * Get Total amount currently stored in the Safe
     *
     * @return Total amount
     */
    public Integer getTotal() {
        Integer fiftyCount = safeRepository.getByBankNote(BankNote.FIFTY).getCount();
        Integer twentyCount = safeRepository.getByBankNote(BankNote.TWENTY).getCount();
        Integer tenCount = safeRepository.getByBankNote(BankNote.TEN).getCount();
        Integer fiveCount = safeRepository.getByBankNote(BankNote.FIVE).getCount();

        return (fiftyCount * 50) + (twentyCount * 20) + (tenCount * 10) + (fiveCount * 5);
    }

    /**
     * Calculate the minimum notes to dispense based on the amount provided.
     *
     * @param amount The amount
     * @return A Map containing the BankNote and the minimum number needed to provide the amount requested.
     * @throws ServiceException {@link ErrorCodes#ATM_FUNDS_ERROR} {@link ErrorMessages#ATM_FUNDS_WITHDRAWAL_ERROR}
     * @throws ServiceException {@link ErrorCodes#ATM_FUNDS_ERROR} {@link ErrorMessages#ATM_FUNDS_AMOUNT_ERROR}
     */
    public Map<BankNote, Integer> calculateNotes(Integer amount) throws ServiceException {
        Map<BankNote, Integer> cashMap = new EnumMap<>(BankNote.class);

        if (amount > getTotal()) {
            throw new ServiceException(ErrorCodes.ATM_FUNDS_ERROR, ErrorMessages.ATM_FUNDS_WITHDRAWAL_ERROR);
        } else if ((amount % 5) > 0) {
            throw new ServiceException(ErrorCodes.ATM_FUNDS_ERROR, ErrorMessages.ATM_FUNDS_AMOUNT_ERROR);
        }

        Integer remainingAmount = amount;
        for (BankNote note : allNotes) {
            Integer bankNotesRequired = remainingAmount / note.getValue();

            Integer bankNotesInSafe = safeRepository.getByBankNote(note).getCount();
            if (bankNotesInSafe < bankNotesRequired) {
                // Only this amount in the safe
                bankNotesRequired = bankNotesInSafe;
            }

            if (bankNotesRequired > 0) {
                remainingAmount = remainingAmount - (bankNotesRequired * note.getValue());
                cashMap.put(note, bankNotesRequired);
                if (remainingAmount == 0) {
                    // All notes counted
                    break;
                }
            }
        }

        if (remainingAmount > 0) {
            throw new ServiceException(ErrorCodes.ATM_FUNDS_ERROR, ErrorMessages.ATM_NOTES_WITHDRAWAL_ERROR);
        }

        return cashMap;
    }

    /**
     * Withdraw cash from the safe
     *
     * @param cashMap A map of notes and associated amounts that have to be withdrawn.
     * @throws ServiceException {@link ErrorCodes#ATM_FUNDS_ERROR} {@link ErrorMessages#ATM_FUNDS_WITHDRAWAL_ERROR}
     */
    public void withdrawCash(Map<BankNote, Integer> cashMap) throws ServiceException {
        logger.debug("withdrawCash: cashMap: {} ", cashMap);

        for (BankNote note : allNotes) {
            Safe safeCash = safeRepository.getByBankNote(note);
            if (cashMap.get(note) != null) {
                safeCash.setCount(safeCash.getCount() - cashMap.get(note));
            }
            if (safeCash.getCount() < 0) {
                throw new ServiceException(ErrorCodes.ATM_FUNDS_ERROR, ErrorMessages.ATM_FUNDS_WITHDRAWAL_ERROR);
            }
            safeRepository.save(safeCash);
        }
    }

    /**
     * This setter method should be used only by unit tests.
     *
     * @param safeRepository Mocked SafeRepository
     */
    protected void setSafeRepository(SafeRepository safeRepository) {
        this.safeRepository = safeRepository;
    }
}
