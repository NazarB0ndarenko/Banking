package com.testproject.banking.repositories;

import com.testproject.banking.entities.Card;
import com.testproject.banking.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setPassword("strongPassword");
        user.setUsername("test");
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setBalance(BigDecimal.valueOf(1000));
        card.setUser(user);
        userRepository.save(user);
        cardRepository.save(card);
    }

    @Test
    void getCardBalance() {
        BigDecimal expected = BigDecimal.valueOf(1000);
        BigDecimal actual = cardRepository.getCardBalance("1234567890123456");
        assertEquals(0, expected.compareTo(actual), "The card balance does not match");
    }


    @Test
    void updateCardBalance() {
        cardRepository.updateCardBalance("1234567890123456", BigDecimal.valueOf(2000));
        BigDecimal expected = BigDecimal.valueOf(2000);
        BigDecimal actual =  cardRepository.getCardBalance("1234567890123456");
        assertEquals(0, expected.compareTo(actual), "The card balance does not match");

    }

    @Test
    void existsByCardNumber_true() {
        assertTrue(cardRepository.existsByCardNumber("1234567890123456"), "existing card was not found");
    }

    @Test
    void existsByCardNumber_false() {
        assertFalse(cardRepository.existsByCardNumber("1234567890123457"), "non-existing card was found");
    }

    @Test
    void getCardOwnerUsername() {
        assertEquals("test", cardRepository.getCardOwnerUsername("1234567890123456"), "cardholder username does not match");
    }
}