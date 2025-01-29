package com.testproject.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class FullTransactionDto {
    private String cardReceiver;
    private String cardSender;
    private BigDecimal amount;
    private Timestamp createdAt;
}

