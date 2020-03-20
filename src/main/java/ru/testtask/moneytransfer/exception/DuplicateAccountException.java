package ru.testtask.moneytransfer.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Unchecked exception to be thrown upon duplicate account creation attempt
 * HTTP status: 400
 */
@NoArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Account with the same identifier already exists")
public class DuplicateAccountException extends RuntimeException {
}
