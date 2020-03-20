package ru.testtask.moneytransfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.testtask.moneytransfer.domain.Account;
import ru.testtask.moneytransfer.domain.Transaction;
import ru.testtask.moneytransfer.dto.TransactionDTO;
import ru.testtask.moneytransfer.exception.AccountNotFoundException;
import ru.testtask.moneytransfer.repository.AccountRepository;
import ru.testtask.moneytransfer.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Transaction service
 */
@Service
@Transactional
public class TransactionService {

    private static final Object tieLock = new Object();
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository,
                              AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    /**
     * Transaction search by identifier
     */
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    /**
     * Transferring amount of money from one account to another.
     * <p>
     * InduceLockOrder
     * Inducing a lock order to avoid deadlock
     * <p>
     * Java concurrency in practice.
     * Listing 10.3
     */
    public Transaction doTransaction(final TransactionDTO transactionDTO) {
        final Long accountFromId = transactionDTO.getFrom();
        final Long accountToId = transactionDTO.getTo();
        final BigDecimal amount = transactionDTO.getAmount();
        class TransferExecutor {
            private Transaction execute() {
                Account accountFrom = accountService.findById(accountFromId).orElseThrow(AccountNotFoundException::new);
                Account accountTo = accountService.findById(accountToId).orElseThrow(AccountNotFoundException::new);

                accountFrom.credit(amount);
                accountTo.debit(amount);

                accountRepository.saveAndFlush(accountFrom);
                accountRepository.saveAndFlush(accountTo);

                return transactionRepository.save(
                        new Transaction(accountFrom.getId(),
                                accountTo.getId(),
                                amount,
                                LocalDateTime.now()
                        )
                );
            }
        }

        int fromHash = System.identityHashCode(accountFromId);
        int toHash = System.identityHashCode(accountToId);

        if (fromHash < toHash) {
            synchronized (accountFromId) {
                synchronized (accountToId) {
                    return new TransferExecutor().execute();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (accountToId) {
                synchronized (accountFromId) {
                    return new TransferExecutor().execute();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (accountFromId) {
                    synchronized (accountToId) {
                        return new TransferExecutor().execute();
                    }
                }
            }
        }
    }
}
