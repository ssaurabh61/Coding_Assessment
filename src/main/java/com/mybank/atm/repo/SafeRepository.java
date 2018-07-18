package com.mybank.atm.repo;

import com.mybank.atm.entity.db.BankNote;
import com.mybank.atm.entity.db.Safe;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA CRUD repository for the Safe
 *
 * @author brian.e.reynolds@outlook.com
 */
public interface SafeRepository extends CrudRepository<Safe, BankNote> {

    /**
     * Get Safe entity by BankNote
     *
     * @param bankNote The banknote to search for
     * @return A Safe entity which represents the notes in the safe.
     */
    Safe getByBankNote(BankNote bankNote);

}
