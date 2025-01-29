package com.testproject.banking.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import com.testproject.banking.dto.CardDto;
import com.testproject.banking.entities.Card;
import com.testproject.banking.exceptions.CardNotFoundException;
import com.testproject.banking.exceptions.NotEnoughFoundsException;
import com.testproject.banking.repositories.CardRepository;
import com.testproject.banking.utility.GeneratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        securityContext.setAuthentication(authentication);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
    }

    @Test
    public void testCreateCard() {
        try (MockedStatic<GeneratorUtils> mockedStatic = mockStatic(GeneratorUtils.class)) {
            mockedStatic.when(GeneratorUtils::generateCardNumber).thenReturn("1234567890123456");
            mockedStatic.when(GeneratorUtils::generateCvv).thenReturn(123);

            CardDto card = cardService.createCard(1L);

            assertThat(card).isNotNull();
            assertThat(card.getCardNumber()).isEqualTo("NAZBON_1234567890123456");
            assertThat(card.getCvv()).isEqualTo(123);
            verify(cardRepository).save(any(Card.class));
        }
    }

    @Test
    public void testTopUpBalance() {
        String cardNumber = "1234567890123456";
        when(cardRepository.getCardBalance(cardNumber)).thenReturn(BigDecimal.ZERO);

        cardService.topUpBalance(cardNumber, BigDecimal.valueOf(100));

        verify(cardRepository).updateCardBalance(cardNumber, BigDecimal.valueOf(100));
    }

    @Test
    public void testWithdrawBalance() throws NotEnoughFoundsException {
        String cardNumber = "1234567890123456";
        when(cardRepository.getCardBalance(cardNumber)).thenReturn(BigDecimal.valueOf(100));

        cardService.withdrawBalance(cardNumber, BigDecimal.valueOf(50));

        verify(cardRepository).updateCardBalance(cardNumber, BigDecimal.valueOf(50));
    }

    @Test
    public void testWithdrawBalanceNotEnoughFunds() {
        String cardNumber = "1234567890123456";
        when(cardRepository.getCardBalance(cardNumber)).thenReturn(BigDecimal.valueOf(50));

        assertThrows(NotEnoughFoundsException.class, () ->
                cardService.withdrawBalance(cardNumber, BigDecimal.valueOf(100)));
    }

    @Test
    public void testCardExists() {
        String cardNumber = "1234567890123456";
        when(cardRepository.existsByCardNumber(cardNumber)).thenReturn(true);

        boolean exists = cardService.cardExists(cardNumber);

        assertThat(exists).isTrue();
    }

    @Test
    public void testGetCardOwnerUsername() throws CardNotFoundException {
        String cardNumber = "1234567890123456";
        when(cardRepository.existsByCardNumber(cardNumber)).thenReturn(true);
        when(cardRepository.getCardOwnerUsername(cardNumber)).thenReturn("testUser");

        String username = cardService.getCardOwnerUsername(cardNumber);

        assertThat(username).isEqualTo("testUser");
    }

    @Test
    public void testGetCardBalance() {
        String cardNumber = "1234567890123456";
        when(cardRepository.getCardBalance(cardNumber)).thenReturn(BigDecimal.valueOf(100));

        BigDecimal balance = cardService.getCardBalance(cardNumber);

        assertThat(balance).isEqualByComparingTo(BigDecimal.valueOf(100));
    }
}
