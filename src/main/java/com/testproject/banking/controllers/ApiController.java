package com.testproject.banking.controllers;

import com.testproject.banking.dto.CardInfoDto;
import com.testproject.banking.dto.TransactionDto;
import com.testproject.banking.services.CardService;
import com.testproject.banking.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final CardService cardService;
    private final TransactionService transactionService;

    @PostMapping("accounts/transfer/external")
    public ResponseEntity<String> makeTransfer(@RequestBody TransactionDto transactionDto) {
        boolean cardExists = cardService.cardExists(transactionDto.getToAccountNumber());

        if (cardExists) {
            cardService.topUpBalance(transactionDto.getToAccountNumber(), transactionDto.getAmount());

            transactionService.saveTransaction(transactionDto);

            return ResponseEntity.ok("Transfer completed successfully");
        }

        return ResponseEntity.badRequest().body("Receiver card not found");
    }

    @PostMapping("accounts/get-cards")
    public ResponseEntity<List<CardInfoDto>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }
}
