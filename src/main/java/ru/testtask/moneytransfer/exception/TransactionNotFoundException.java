package ru.testtask.moneytransfer.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Unchecked exception to be thrown when transaction cannot be found in database
 */
@NoArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Transaction not found")
public class TransactionNotFoundException extends NotFoundException {
}
