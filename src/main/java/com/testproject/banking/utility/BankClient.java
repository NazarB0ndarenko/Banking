package com.testproject.banking.utility;

import com.testproject.banking.dto.TransactionDto;

public interface BankClient {
    void createPayment(TransactionDto payment);
}

