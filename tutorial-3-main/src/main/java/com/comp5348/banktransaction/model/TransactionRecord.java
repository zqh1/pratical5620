package com.comp5348.banktransaction.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity object for transaction_record database table.
 */
@Getter
@NoArgsConstructor
@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime times;

    @ManyToOne
    @JoinColumn
    private Account toAccount;

    @ManyToOne
    @JoinColumn
    private Account fromAccount;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn
    private Account bankAccount;

    @Column
    private Double merchantFeeAmount;

    public TransactionRecord(Double amount, Account toAccount, Account fromAccount, Account bankAccount, Double merchantFeeAmount) {
        this.amount = amount;
        this.times = LocalDateTime.now();
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
        this.bankAccount = bankAccount;
        this.merchantFeeAmount = merchantFeeAmount;
    }
}
