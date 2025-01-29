package com.testproject.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CardInfoDto {
    private String  cardNumber;
    private BigDecimal balance;
}
