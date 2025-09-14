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
    @Column(name="account_id")
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "customer_id")
    private Customer customer;

    @Version
    private int version;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "balance")
    private Double balance = 0.0;

    @OneToMany(mappedBy = "fromAccount")
    private Collection<TransactionRecord> fromTransactionRecords;

    @OneToMany(mappedBy = "toAccount")
    private Collection<TransactionRecord> toTransactionRecords;

    //Add account type and merchantfee percentage to identify the account type and merchant fee
    @Column(nullable = false, name = "merchant_fee_percentage")
    @Setter
    private Double merchantFeePercentage = 0.0;

    @Column(nullable = false,name = "account_type")
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
