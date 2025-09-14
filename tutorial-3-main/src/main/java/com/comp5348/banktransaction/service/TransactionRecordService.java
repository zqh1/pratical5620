package com.comp5348.banktransaction.service;

import com.comp5348.banktransaction.dto.TransactionRecordDTO;
import com.comp5348.banktransaction.errors.InsufficientBalanceException;
import com.comp5348.banktransaction.errors.NegativeTransferAmountException;
import com.comp5348.banktransaction.model.Account;
import com.comp5348.banktransaction.model.TransactionRecord;
import com.comp5348.banktransaction.repository.AccountRepository;
import com.comp5348.banktransaction.repository.CustomerRepository;
import com.comp5348.banktransaction.repository.TransactionRecordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Business logic for creating and managing transactions (transfer / deposit).
 */
@Service
public class TransactionRecordService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    @PersistenceContext
    private EntityManager entityManager;



    @Autowired
    public TransactionRecordService(AccountRepository accountRepository, CustomerRepository customerRepository, TransactionRecordRepository transactionRecordRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    @Transactional
    public TransactionRecordDTO performTransaction(
            Long fromCustomerId, Long fromAccountId,
            Long toCustomerId, Long toAccountId,Long bankAccountId,
            Double amount)
            throws InsufficientBalanceException, HttpClientErrorException {
        if (amount <= 0) {
            throw new NegativeTransferAmountException();
        }

        //Add to count merchant fee and the amount should send to toAccount
        double merchantFee = 0.0;
        double amountToReceiver = 0;

        Account fromAccount = null;
        if (fromAccountId != null) {
            fromAccount = accountRepository
                    .findByIdAndCustomer(fromAccountId, customerRepository.getReferenceById(fromCustomerId))
                    .orElseThrow();
            entityManager.refresh(fromAccount);

            if (fromAccount.getBalance() < amount) {
                throw new InsufficientBalanceException();
            }
            fromAccount.modifyBalance(-amount);
            accountRepository.save(fromAccount);
        }

        Account toAccount = null;
        if (toAccountId != null) {
            //add to see if the receiver account is a business account
            amountToReceiver = amount;

            toAccount = accountRepository
                    .findByIdAndCustomer(toAccountId, customerRepository.getReferenceById(toCustomerId))
                    .orElseThrow();
            if ("BUSINESS".equalsIgnoreCase(toAccount.getAccountType())) {
                Double pct = toAccount.getMerchantFeePercentage();
                if (pct != null && pct > 0) {
                    merchantFee = amount * pct;
                    amountToReceiver = amount - merchantFee;
                }
            }
            toAccount.modifyBalance(amountToReceiver);
            accountRepository.save(toAccount);
        }

        //add the bank account details
        Account bankAccount = null;
        if (merchantFee > 0) {
            bankAccount = accountRepository.findById(bankAccountId).orElseThrow();
            bankAccount.modifyBalance(merchantFee);
            accountRepository.save(bankAccount);
        }


        TransactionRecord transactionRecord = new TransactionRecord(amount, toAccount, fromAccount,bankAccount,merchantFee);
        transactionRecordRepository.save(transactionRecord);

        return new TransactionRecordDTO(transactionRecord);
    }


}
