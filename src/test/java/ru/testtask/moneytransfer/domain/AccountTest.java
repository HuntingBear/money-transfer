package ru.testtask.moneytransfer.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.testtask.moneytransfer.exception.InsufficientFundsException;
import ru.testtask.moneytransfer.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AccountTest {
    private static final long ACCOUNT_ID = 101L;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void init() {
        accountRepository.deleteAll();
        accountRepository.save(new Account(ACCOUNT_ID, BigDecimal.valueOf(1000)));
    }

    @AfterEach
    void clear() {
        accountRepository.deleteAll();
    }

    @Test
    void debitAccountTest() {
        Optional<Account> optionalAccount = accountRepository.findById(ACCOUNT_ID);
        assertEquals(BigDecimal.valueOf(1100), optionalAccount.get().debit(BigDecimal.valueOf(100)));
    }

    @Test
    void creditAccountTest() {
        Optional<Account> optionalAccount = accountRepository.findById(ACCOUNT_ID);
        assertEquals(BigDecimal.valueOf(900), optionalAccount.get().credit(BigDecimal.valueOf(100)));
    }

    @Test
    void creditInsufficientFundsThrowsExceptionTest() {
        Optional<Account> optionalAccount = accountRepository.findById(ACCOUNT_ID);
        assertThrows(InsufficientFundsException.class, () -> optionalAccount.get().credit(BigDecimal.valueOf(1001)));
    }
}
