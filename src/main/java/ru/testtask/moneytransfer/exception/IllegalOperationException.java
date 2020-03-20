package ru.testtask.moneytransfer.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Unchecked exception to be thrown when some illegal operation happened.
 * HTTP status: 400
 */
@NoArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Operation can't be performed")
public class IllegalOperationException extends RuntimeException {
}
