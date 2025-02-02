package com.testproject.banking.controllers;

import com.testproject.banking.dto.ChangeCardBalanceDto;
import com.testproject.banking.dto.FullTransactionDto;
import com.testproject.banking.dto.TransactionDto;
import com.testproject.banking.exceptions.CardNotFoundException;
import com.testproject.banking.exceptions.NotEnoughFoundsException;
import com.testproject.banking.services.BankClientService;
import com.testproject.banking.services.CardService;
import com.testproject.banking.services.TransactionService;
import com.testproject.banking.services.UserService;
import com.testproject.banking.utility.BankClient;
import io.swagger.v3.oas.models.examples.Example;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@Transactional
@RequestMapping ("/user")
public class UserController {

    private final CardService cardService;
    private final BankClientService bankClientService;
    private final TransactionService transactionService;

    @PostMapping("/make-transaction")
    public ResponseEntity<String> makeTransaction(@RequestBody TransactionDto transactionDto) {

        if (!isUserOwnerOfCard(transactionDto.getFromAccountNumber())) {
                return ResponseEntity.badRequest().body("You send money from other user's card");

        }

        boolean cardExists = cardService.cardExists(transactionDto.getToAccountNumber());
        log.info("card: {} exist is out system - {}", transactionDto.getToAccountNumber(), cardExists);

        if (cardExists) {

            cardService.withdrawBalance(transactionDto.getFromAccountNumber(), transactionDto.getAmount());
            cardService.topUpBalance(transactionDto.getToAccountNumber(), transactionDto.getAmount());

            transactionService.saveTransaction(transactionDto);
            TransactionDto transactionDto1 = new TransactionDto(transactionDto.getAmount(), transactionDto.getToAccountNumber(), transactionDto.getFromAccountNumber());
            transactionDto.setAmount(transactionDto.getAmount().multiply(BigDecimal.valueOf(-1)));

            transactionService.saveTransaction(transactionDto1);
            transactionService.saveTransaction(transactionDto);

            return ResponseEntity.ok("Transaction completed successfully");
        } else {

            log.info("searching for bank with id: {}", transactionDto.getToAccountNumber().substring(0, 6));

            BankClient receiver = bankClientService.getClientByCardNumber(transactionDto.getToAccountNumber());
            if (receiver != null) {

                log.info("searching for feign client for bank {}", transactionDto.getToAccountNumber().substring(0, 6));

                receiver.createPayment(transactionDto);
                cardService.withdrawBalance(transactionDto.getFromAccountNumber(), transactionDto.getAmount());

                transactionDto.setAmount(transactionDto.getAmount().multiply(BigDecimal.valueOf(-1)));
                transactionService.saveTransaction(transactionDto);
                return ResponseEntity.ok("Transaction completed successfully");
            }
        }

        return ResponseEntity.badRequest().body("Receiver card not found");
    }

    @PostMapping("/top-up-balance")
    public ResponseEntity<String> topUpBalance(@RequestBody ChangeCardBalanceDto changeCardBalanceDto) {

        log.info("Topping up balance for card {}", changeCardBalanceDto.getCardNumber());

        if (!isUserOwnerOfCard(changeCardBalanceDto.getCardNumber())){
            log.info("User is not owner of the card");
            return ResponseEntity.badRequest().body("You can't top up balance of other user's card");
        }

        log.info("User is owner of the card");
        cardService.topUpBalance(changeCardBalanceDto.getCardNumber(), changeCardBalanceDto.getAmount());

        TransactionDto transactionDto = new TransactionDto(changeCardBalanceDto.getAmount(), changeCardBalanceDto.getCardNumber(), "Top up balance");
        transactionService.saveTransaction(transactionDto);

        log.info("Balance topped up for card {}", changeCardBalanceDto.getCardNumber());
        BigDecimal balance = cardService.getCardBalance(changeCardBalanceDto.getCardNumber());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Balance: " + balance);
    }

    @PostMapping("/withdraw-balance")
    public ResponseEntity<String> withdrawBalance(@RequestBody ChangeCardBalanceDto changeCardBalanceDto) {
        if (!isUserOwnerOfCard(changeCardBalanceDto.getCardNumber())) {
            return ResponseEntity.badRequest().body("You can't withdraw balance of other user's card");
        }

        cardService.withdrawBalance(changeCardBalanceDto.getCardNumber(), changeCardBalanceDto.getAmount());

        TransactionDto transactionDto = new TransactionDto(changeCardBalanceDto.getAmount().multiply(BigDecimal.valueOf(-1)), changeCardBalanceDto.getCardNumber(), "Withdraw balance");
        transactionService.saveTransaction(transactionDto);

        BigDecimal balance = cardService.getCardBalance(changeCardBalanceDto.getCardNumber());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Balance: " + balance);
    }

    @GetMapping
    public ResponseEntity<String> getBalance(@RequestParam String cardNumber) {
        if (!isUserOwnerOfCard(cardNumber)) {
            return ResponseEntity.badRequest().body("You can't get balance of other user's card");
        }

        BigDecimal balance = cardService.getCardBalance(cardNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Balance: " + balance);
    }

    @GetMapping("/transactions")
    private ResponseEntity getTransactions(@RequestParam String cardNumber) {
        if (!isUserOwnerOfCard(cardNumber)) {
            return ResponseEntity.badRequest().body("You can't get balance of other user's card");
        }

        return ResponseEntity.ok(transactionService.getCardTransactions(cardNumber));
    }

    private boolean isUserOwnerOfCard(String cardNumber) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String cardOwner = cardService.getCardOwnerUsername(cardNumber);

        return username.equals(cardOwner);
    }
}
