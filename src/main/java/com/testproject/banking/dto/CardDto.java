package com.testproject.banking.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class CardDto {
    private String  cardNumber;
    private int cvv;
    private BigDecimal balance;
}
