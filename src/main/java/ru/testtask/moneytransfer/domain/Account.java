package ru.testtask.moneytransfer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.testtask.moneytransfer.converter.BalanceConverter;
import ru.testtask.moneytransfer.exception.IllegalOperationException;
import ru.testtask.moneytransfer.exception.InsufficientFundsException;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Account entity
 */
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class Account {
    @Id
    @NonNull
    private long id;
    @NonNull
    @Column(nullable = false)
    @Convert(converter = BalanceConverter.class)
    private BigDecimal balance;

    /**
     * Credits account balance
     *
     * @param amount Amount of money to be credited from account balance
     * @return Account balance after credit
     */
    public BigDecimal credit(BigDecimal amount) {
        validate(amount);
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        this.balance = balance.subtract(amount);
        return balance;
    }

    /**
     * Debits account balance
     *
     * @param amount Amount of money to be debited to account balance
     * @return Account balance after credit
     */
    public BigDecimal debit(BigDecimal amount) {
        validate(amount);
        this.balance = balance.add(amount);
        return balance;
    }

    /**
     * Checks if amount of money to be credited or debited is not null and positive
     *
     * @param amount Amount of money
     */
    private void validate(BigDecimal amount) {
        if (Objects.isNull(amount) || BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalOperationException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        Transaction Transaction = (Transaction) o;
        return Objects.equals(getId(), Transaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
