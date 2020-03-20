package ru.testtask.moneytransfer.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.testtask.moneytransfer.domain.Account;
import ru.testtask.moneytransfer.domain.Transaction;
import ru.testtask.moneytransfer.dto.TransactionDTO;
import ru.testtask.moneytransfer.exception.AccountNotFoundException;
import ru.testtask.moneytransfer.repository.AccountRepository;
import ru.testtask.moneytransfer.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionServiceTest {

    private final static Logger log = LoggerFactory.getLogger(TransactionServiceTest.class);

    private static final long SECOND_ACCOUNT_ID = 2L;
    private static final long FIRST_ACCOUNT_ID = 1L;
    private static final int TRANSFERS_NUMBER = 100;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @BeforeEach
    void init() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();

        accountService.addAccount(new Account(FIRST_ACCOUNT_ID, BigDecimal.valueOf(5000)));
        accountService.addAccount(new Account(SECOND_ACCOUNT_ID, BigDecimal.valueOf(1000)));
    }

    @AfterEach
    void clear() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void simpleTransactionTest() {
        TransactionDTO transactionDTO = new TransactionDTO(FIRST_ACCOUNT_ID, SECOND_ACCOUNT_ID,
                BigDecimal.valueOf(5000));
        transactionService.doTransaction(transactionDTO);

        assertEquals(BigDecimal.ZERO, accountService.findById(FIRST_ACCOUNT_ID)
                .orElseThrow(AccountNotFoundException::new)
                .getBalance());
        assertEquals(BigDecimal.valueOf(6000), accountService.findById(SECOND_ACCOUNT_ID)
                .orElseThrow(AccountNotFoundException::new)
                .getBalance());
    }

    @Test
    void simpleSimultaneouslyTransactionsTest() {
        CompletableFuture straightMoneyTransfer = CompletableFuture.runAsync(() -> {
            TransactionDTO straightTransactionDTO = new TransactionDTO(FIRST_ACCOUNT_ID, SECOND_ACCOUNT_ID,
                    BigDecimal.valueOf(10));
            Transaction straightTrx = transactionService.doTransaction(straightTransactionDTO);
            log.info("[Thread-" + Thread.currentThread().getId() + "] " + straightTrx);
        });

        CompletableFuture counterMoneyTransfer = CompletableFuture.runAsync(() -> {
            TransactionDTO counterTransactionDTO = new TransactionDTO(SECOND_ACCOUNT_ID, FIRST_ACCOUNT_ID,
                    BigDecimal.valueOf(25));
            Transaction counterTrx = transactionService.doTransaction(counterTransactionDTO);
            log.info("[Thread-" + Thread.currentThread().getId() + "] " + counterTrx);
        });

        CompletableFuture.allOf(straightMoneyTransfer, counterMoneyTransfer).join();

        assertEquals(2, transactionRepository.count());
        assertEquals(BigDecimal.valueOf(5015), accountService.findById(FIRST_ACCOUNT_ID)
                .orElseThrow(AccountNotFoundException::new)
                .getBalance());
        assertEquals(BigDecimal.valueOf(985), accountService.findById(SECOND_ACCOUNT_ID)
                .orElseThrow(AccountNotFoundException::new)
                .getBalance());
    }

    @Test
    void heavyLoadedSimultaneouslyTransactionsTest() throws InterruptedException {
        Runnable straightTransfer = () -> {
            TransactionDTO straightTransactionDTO = new TransactionDTO(FIRST_ACCOUNT_ID, SECOND_ACCOUNT_ID,
                    BigDecimal.valueOf(10));
            Transaction straightTrx = transactionService.doTransaction(straightTransactionDTO);
            log.info("[Thread-" + Thread.currentThread().getId() + "] " + straightTrx);
        };
        Runnable counterTransfer = () -> {
            TransactionDTO counterTransactionDTO = new TransactionDTO(SECOND_ACCOUNT_ID, FIRST_ACCOUNT_ID,
                    BigDecimal.valueOf(3));
            Transaction counterTrx = transactionService.doTransaction(counterTransactionDTO);
            log.info("[Thread-" + Thread.currentThread().getId() + "] " + counterTrx);
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < TRANSFERS_NUMBER; i++) {
            executorService.submit(straightTransfer);
            executorService.submit(counterTransfer);
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(BigDecimal.valueOf(4300), accountService.findById(FIRST_ACCOUNT_ID)
                .orElseThrow(AccountNotFoundException::new)
                .getBalance());
        assertEquals(BigDecimal.valueOf(1700), accountService.findById(SECOND_ACCOUNT_ID)
                .orElseThrow(AccountNotFoundException::new)
                .getBalance());
        assertEquals(200, transactionRepository.count());
    }
}
