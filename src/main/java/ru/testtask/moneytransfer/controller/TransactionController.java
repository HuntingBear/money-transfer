package ru.testtask.moneytransfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.testtask.moneytransfer.domain.Transaction;
import ru.testtask.moneytransfer.dto.TransactionDTO;
import ru.testtask.moneytransfer.service.TransactionService;

/**
 * Rest controller for transactions
 */
@RepositoryRestController
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Creates new transaction
     */
    @ResponseBody
    @RequestMapping(value = "/transactions",
            method = RequestMethod.POST,
            produces = "application/hal+json")
    public PersistentEntityResource transaction(@RequestBody TransactionDTO transactionDTO,
                                                PersistentEntityResourceAssembler assembler) {
        return assembler.toFullResource(transactionService.doTransaction(transactionDTO));
    }
}
