package ru.testtask.moneytransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.testtask.moneytransfer.domain.Transaction;

import java.util.List;

/**
 * Repository interface for transaction with paging and sorting support
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Default creation and edit prohibition
     */
    @RestResource(exported = false)
    @Override
    <S extends Transaction> S save(S s);

    /**
     * Default creation and edit prohibition
     */
    @RestResource(exported = false)
    @Override
    <S extends Transaction> List<S> saveAll(Iterable<S> iterable);

    /**
     * Default creation and edit prohibition
     */
    @RestResource(exported = false)
    @Override
    <S extends Transaction> S saveAndFlush(S s);

    /**
     * Default deletion prohibition
     */
    @RestResource(exported = false)
    @Override
    void deleteById(Long aLong);

    /**
     * Default deletion prohibition
     */
    @RestResource(exported = false)
    @Override
    void delete(Transaction transaction);

    /**
     * Default deletion prohibition
     */
    @RestResource(exported = false)
    @Override
    void deleteAll(Iterable<? extends Transaction> iterable);

    /**
     * Default deletion prohibition
     */
    @RestResource(exported = false)
    @Override
    void deleteAll();
}
