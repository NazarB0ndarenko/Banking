package com.testproject.banking.entities;

import com.testproject.banking.dto.TransactionDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String cardReceiver;
    private String cardSender;
    private BigDecimal amount;

    private Timestamp createdAt;

    public Transaction(TransactionDto transactionDto) {
        this.cardReceiver = transactionDto.getToAccountNumber();
        this.cardSender = transactionDto.getFromAccountNumber();
        this.amount = transactionDto.getAmount();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }
}
