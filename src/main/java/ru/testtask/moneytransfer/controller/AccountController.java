package ru.testtask.moneytransfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.testtask.moneytransfer.domain.Account;
import ru.testtask.moneytransfer.dto.AccountDTO;
import ru.testtask.moneytransfer.service.AccountService;

import java.math.BigDecimal;

/**
 * Rest controller for accounts
 */
@RepositoryRestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Creates new account
     */
    @ResponseBody
    @RequestMapping(value = "/accounts",
            method = RequestMethod.POST,
            produces = "application/hal+json")
    public PersistentEntityResource accountCreation(@RequestBody AccountDTO accountDTO,
                                                    PersistentEntityResourceAssembler assembler) {
        Account account = parseAccountCreation(accountDTO);
        return assembler.toFullResource(accountService.addAccount(account));
    }

    private static Account parseAccountCreation(final AccountDTO accountDTO) {
        return new Account(accountDTO.getId(),
                accountDTO.getBalance() != null ? accountDTO.getBalance() : BigDecimal.ZERO);
    }
}
