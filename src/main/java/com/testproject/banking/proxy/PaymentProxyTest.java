package com.testproject.banking.proxy;

import com.testproject.banking.dto.TransactionDto;
import com.testproject.banking.utility.BankClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "paymentsTest", url = "${name.service.test.url}")
public interface PaymentProxyTest extends BankClient {

    @Override
    @PostMapping("/payment")
    void createPayment(TransactionDto payment);
}
