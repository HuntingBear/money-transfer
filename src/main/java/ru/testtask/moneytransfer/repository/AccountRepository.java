package ru.testtask.moneytransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.testtask.moneytransfer.domain.Account;

import java.util.List;

/**
 * Repository interface for account with paging and sorting support
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * Default creation and edit prohibition
     */
    @RestResource(exported = false)
    @Override
    <S extends Account> S save(S s);

    /**
     * Default creation and edit prohibition
     */
    @RestResource(exported = false)
    @Override
    <S extends Account> List<S> saveAll(Iterable<S> iterable);

    /**
     * Default creation and edit prohibition
     */
    @RestResource(exported = false)
    @Override
    <S extends Account> S saveAndFlush(S s);
}
