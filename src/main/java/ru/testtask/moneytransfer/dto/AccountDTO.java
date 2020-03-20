package ru.testtask.moneytransfer.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Account data transfer object.
 */
@Getter
@Setter
public final class AccountDTO {
    private int id;
    private BigDecimal balance;

    public AccountDTO(int id, BigDecimal balance) {
        this.id = id;
        if (balance == null) {
            this.balance = BigDecimal.ZERO;
        } else {
            this.balance = balance;
        }
    }
}
