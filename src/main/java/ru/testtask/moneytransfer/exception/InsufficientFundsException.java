package ru.testtask.moneytransfer.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Unchecked exception to be thrown when account haven't enough funds to be credited.
 * HTTP status: 400
 */

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Insufficient funds to make transfer")
public class InsufficientFundsException extends RuntimeException {
}
