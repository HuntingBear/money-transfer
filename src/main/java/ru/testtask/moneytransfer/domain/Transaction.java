package ru.testtask.moneytransfer.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.testtask.moneytransfer.converter.BalanceConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transaction entity
 */
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class Transaction {
    @Id
    @GeneratedValue
    private long id;
    @NonNull
    @Column(nullable = false)
    private long fromAccount;
    @NonNull
    @Column(nullable = false)
    private long toAccount;
    @Column(nullable = false)
    @NonNull
    @Convert(converter = BalanceConverter.class)
    private BigDecimal amount;
    @NonNull
    @Column
    private LocalDateTime time;

    public void setFromAccount(long fromAccount) {
        this.fromAccount = fromAccount;
    }

    public void setToAccount(long toAccount) {
        this.toAccount = toAccount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", amount=" + amount +
                ", time=" + time +
                '}';
    }
}
