package com.testproject.banking.services;

import com.testproject.banking.dto.CardDto;
import com.testproject.banking.dto.CardInfoDto;
import com.testproject.banking.exceptions.CardNotFoundException;
import com.testproject.banking.exceptions.NotEnoughFoundsException;
import com.testproject.banking.utility.GeneratorUtils;
import com.testproject.banking.entities.Card;
import com.testproject.banking.repositories.CardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public CardDto createCard(long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("Creating a new card for user {}", username);
        String cardNumber = "NAZBON_" + GeneratorUtils.generateCardNumber();
        int cvv = GeneratorUtils.generateCvv();
        Card card = new Card(cardNumber, cvv, userId);

        log.info("Saving the card to the database for user {}", username);
        cardRepository.save(card);

        log.info("card {} created for user {}", cardNumber, username);
        CardDto cardDto = new CardDto(cardNumber, cvv, new BigDecimal(0));
        return cardDto;
    }

    public void topUpBalance(String cardNumber, BigDecimal amount) throws IllegalArgumentException{
        log.info("Topping up balance for card {}", cardNumber);

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        BigDecimal currentBalance = cardRepository.getCardBalance(cardNumber);

        BigDecimal newBalance = currentBalance.add(amount);
        cardRepository.updateCardBalance(cardNumber, newBalance);
        log.info("Balance topped up for card {}", cardNumber);
    }

    public void withdrawBalance(String cardNumber, BigDecimal amount) throws NotEnoughFoundsException {
        log.info("Withdrawing balance for card {}", cardNumber);
        BigDecimal currentBalance = cardRepository.getCardBalance(cardNumber);

        BigDecimal newBalance = currentBalance.subtract(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughFoundsException("Insufficient balance");
        }

        cardRepository.updateCardBalance(cardNumber, newBalance);
        log.info("Balance withdrawn for card {}", cardNumber);
    }

    public boolean cardExists(String cardNumber) {
        return cardRepository.existsByCardNumber(cardNumber);
    }

    public String getCardOwnerUsername(String cardNumber) throws CardNotFoundException {
        log.info("Getting card owner username for card {}", cardNumber);

        if (!cardExists(cardNumber)) {
            throw new CardNotFoundException("Card not found");
        }

        String username = cardRepository.getCardOwnerUsername(cardNumber);
        log.info("Card owner username for card {} is {}", cardNumber, username);
        return username;
    }

    public BigDecimal getCardBalance(String cardNumber) {
        log.info("Getting card balance for card {}", cardNumber);
        return cardRepository.getCardBalance(cardNumber);
    }

    public List<CardInfoDto> getAllCards() {
        log.info("Getting all cards");
        return cardRepository.getAllCards();
    }
}
