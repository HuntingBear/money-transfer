package ru.testtask.moneytransfer.controller;

import io.restassured.http.ContentType;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import ru.testtask.moneytransfer.domain.Account;
import ru.testtask.moneytransfer.exception.DuplicateAccountException;
import ru.testtask.moneytransfer.repository.AccountRepository;
import ru.testtask.moneytransfer.service.AccountService;

import java.math.BigDecimal;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        accountRepository.deleteAll();

        accountService.addAccount(new Account(1, BigDecimal.valueOf(1000)));
        accountService.addAccount(new Account(2, BigDecimal.valueOf(2000)));
        accountService.addAccount(new Account(3, BigDecimal.valueOf(3000)));
    }

    @AfterEach
    void clear() {
        accountRepository.deleteAll();
    }

    @Test
    void duplicateAccountCreationThrowsException() {
        assertThrows(DuplicateAccountException.class,
                () -> accountService.addAccount(new Account(1, BigDecimal.valueOf(1000))));
    }

    @Test
    void requestExistingAccount() {
        expect().statusCode(200)
                .contentType(ContentType.JSON)
                .given()
                .port(port)
                .when()
                .log().uri()
                .get("/accounts/1");
    }

    @Test
    void requestNotExistingAccount() {
        expect().statusCode(404)
                .given()
                .port(port)
                .get("/accounts/100");
    }

    @Test
    void requestAllAccounts() {
        given().port(port)
                .when()
                .get("/accounts")
                .then()
                .contentType(ContentType.JSON)
                .body("page.totalElements", Is.is(3));
    }

    @Test
    void requestOneAccount() {
        given().port(port)
                .when()
                .get("/accounts/1")
                .then()
                .contentType(ContentType.JSON)
                .body("id", Is.is(1))
                .body("balance", Is.is(1000));
    }

    @Test
    void addNewAccount() {
        given().port(port)
                .contentType(ContentType.JSON)
                .body(new Account(4, BigDecimal.valueOf(4000)))
                .when()
                .post("/accounts")
                .then()
                .statusCode(200)
                .body("id", Is.is(4))
                .body("balance", Is.is(4000));
    }
}
