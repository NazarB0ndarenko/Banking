package com.testproject.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDto {
    private BigDecimal amount;
    private String fromAccountNumber;
    private String toAccountNumber;
}