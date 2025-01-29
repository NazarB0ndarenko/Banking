package com.testproject.banking.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ChangeCardBalanceDto {
    private String cardNumber;
    private BigDecimal amount;
}
