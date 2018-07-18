package com.mybank.atm.repo;

import com.mybank.atm.entity.db.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Spring Data JPA CRUD repository for Accounts
 *
 * @author brian.e.reynolds@outlook.com
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    /**
     * Get a list of accounts by account number
     *
     * @param accountNumber The customer account number
     * @return List of Accounts
     */
    List<Account> findByAccountNumber(Long accountNumber);
}
