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
    @Column(name = "transactional_id")
    private long id;

    @Column(nullable = false, name = "amount")
    private Double amount;

    @Column(nullable = false, name = "times")
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private Account bankAccount;

    @Column(name ="merchant_fee")
    private Double merchantFeeAmount;

    public TransactionRecord(Double amount, Account toAccount, Account fromAccount, Account bankAccount, Double merchantFeeAmount) {
        this.amount = amount;
        this.time = LocalDateTime.now();
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
        this.bankAccount = bankAccount;
        this.merchantFeeAmount = merchantFeeAmount;
    }
}
