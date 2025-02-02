package com.testproject.banking.repositories;

import com.testproject.banking.entities.Card;
import com.testproject.banking.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CardRepositoryTest {

    @Container
    private static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0");

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setPassword("strongPassword");
        user.setUsername("test");
        user = userRepository.save(user);

        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setBalance(BigDecimal.valueOf(1000));
        card.setUser(user);
        cardRepository.save(card);
    }

    @AfterEach
    void tearDown() {
        cardRepository.deleteAll();
        userRepository.deleteAll();
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