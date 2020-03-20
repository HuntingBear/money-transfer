package ru.testtask.moneytransfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.testtask.moneytransfer.domain.Account;
import ru.testtask.moneytransfer.exception.DuplicateAccountException;
import ru.testtask.moneytransfer.repository.AccountRepository;

import java.util.Optional;

/**
 * Account service
 */
@Service
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * New account creation
     */
    public Account addAccount(Account account) {
        if (accountRepository.findById(account.getId()).isPresent()) {
            throw new DuplicateAccountException();
        } else {
            return accountRepository.save(account);
        }
    }

    /**
     * Account search by identifier
     */
    public Optional<Account> findById(long id) {
        return accountRepository.findById(id);
    }
}
