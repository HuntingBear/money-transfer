package ru.testtask.moneytransfer.controller;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import ru.testtask.moneytransfer.domain.Account;
import ru.testtask.moneytransfer.domain.Transaction;
import ru.testtask.moneytransfer.dto.TransactionDTO;
import ru.testtask.moneytransfer.repository.TransactionRepository;
import ru.testtask.moneytransfer.service.AccountService;
import ru.testtask.moneytransfer.service.TransactionService;

import java.math.BigDecimal;

import static io.restassured.RestAssured.expect;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    AccountService accountService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        transactionRepository.deleteAll();
    }

    @AfterEach()
    void clear() {
        transactionRepository.deleteAll();
    }

    @Test
    void requestExistingTransaction() {
        Account accountFrom = accountService.addAccount(new Account(1001L, BigDecimal.valueOf(100)));
        Account accountTo = accountService.addAccount(new Account(1002L, BigDecimal.valueOf(200)));
        TransactionDTO transactionDTO = new TransactionDTO(accountFrom.getId(), accountTo.getId(),
                BigDecimal.valueOf(100));
        Transaction transaction = transactionService.doTransaction(transactionDTO);

        expect().statusCode(200)
                .contentType(ContentType.JSON)
                .given()
                .port(port)
                .when()
                .get("/transactions/" + transaction.getId())
                .then()
                .body("amount", Is.is(100))
                .body("time", Is.is(Matchers.notNullValue()));

    }
}
