package com.testproject.banking.services;

import com.testproject.banking.dto.FullTransactionDto;
import com.testproject.banking.dto.TransactionDto;
import com.testproject.banking.entities.Transaction;
import com.testproject.banking.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<FullTransactionDto> getCardTransactions(String cardNumber) {
        log.info("Getting transactions for card number: {}", cardNumber);
        return transactionRepository.getTransactionsByCardReceiver(cardNumber);
    }

    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = new Transaction(transactionDto);
        transactionRepository.save(transaction);
    }

}
