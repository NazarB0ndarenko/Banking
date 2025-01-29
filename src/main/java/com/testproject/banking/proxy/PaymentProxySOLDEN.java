package com.testproject.banking.proxy;

import com.testproject.banking.dto.TransactionDto;
import com.testproject.banking.utility.BankClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "paymentsSOLDEN", url = "${name.service.SOLDEN.url}")
public interface PaymentProxySOLDEN extends BankClient {

    @Override
    @PostMapping("api/v1/accounts/transfer/external")
    void createPayment(TransactionDto payment);
}
