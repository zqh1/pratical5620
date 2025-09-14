package com.comp5348.banktransaction.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Entity object for account database table.
 */
@Getter
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    @Version
    private int version;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double balance = 0.0;

    @OneToMany(mappedBy = "fromAccount")
    private Collection<TransactionRecord> fromTransactionRecords;

    @OneToMany(mappedBy = "toAccount")
    private Collection<TransactionRecord> toTransactionRecords;

    //Add account type and merchantfee percentage to identify the account type and merchant fee
    @Column(nullable = false)
    @Setter
    private Double merchantFeePercentage = 0.0;

    @Column(nullable = false)
    private String accountType;

    //Add account type and merchant fee percentage to the constructor
    public Account(Customer customer, String name,String accountType,Double merchantFeePercentage) {
        this.customer = customer;
        this.name = name;
        this.accountType = accountType;
        this.fromTransactionRecords = new ArrayList<>();
        this.toTransactionRecords = new ArrayList<>();
        this.merchantFeePercentage = merchantFeePercentage;
    }

    public void modifyBalance(Double amount) {
        this.balance = this.balance + amount;
    }

}
