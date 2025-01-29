package com.testproject.banking.services;

import com.testproject.banking.proxy.PaymentProxySOLDEN;
import com.testproject.banking.proxy.PaymentProxyTest;
import com.testproject.banking.utility.BankClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BankClientService {

    private final Map<String, BankClient> bankClientMap;

    @Autowired
    public BankClientService(PaymentProxySOLDEN SOLDENBank,
                             PaymentProxyTest testBank) {
        bankClientMap = new HashMap<>();
        bankClientMap.put("SOLDEN", SOLDENBank);
        bankClientMap.put("TEST11", testBank);
    }

    public BankClient getClientByCardNumber(String cardNumber) {
        String prefix = cardNumber.substring(0, 6);
        return bankClientMap.get(prefix);
    }
}
