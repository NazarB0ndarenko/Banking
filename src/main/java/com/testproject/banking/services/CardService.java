package com.testproject.banking.servises;

import com.testproject.banking.entities.Card;
import com.testproject.banking.repositories.CardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public Card createCard(long userId) {
        String username  = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("Creating a new card for user {}", username );
        String cardNumber = GeneratorService.generateCardNumber();
        int cvv = GeneratorService.generateCvv();
        Card card = new Card(cardNumber, cvv, userId);

        log.info("Saving the card to the database for user {}", username );
        cardRepository.save(card);

        log.info("card {} created for user {}", cardNumber, username );
        return card;
    }
}
