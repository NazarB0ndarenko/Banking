package com.testproject.banking.repositories;

import com.testproject.banking.dto.FullTransactionDto;
import com.testproject.banking.dto.TransactionDto;
import com.testproject.banking.entities.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepositories extends CrudRepository<Transaction, Long> {

    @Query("SELECT new com.testproject.banking.dto.FullTransactionDto(t.cardReceiver, t.cardSender, t.amount, t.createdAt) " +
            "FROM Transaction t " +
            "WHERE t.cardReceiver = :cardReceiver")
    List<FullTransactionDto> getTransactionsByCardReceiver(String cardReceiver);

}
