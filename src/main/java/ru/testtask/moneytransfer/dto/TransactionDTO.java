package ru.testtask.moneytransfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Transaction data transfer object.
 * Transaction id and time are excluded
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class TransactionDTO {
    private long from;
    private long to;
    private BigDecimal amount;
}
