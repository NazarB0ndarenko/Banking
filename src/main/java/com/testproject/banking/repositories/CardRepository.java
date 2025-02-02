package com.testproject.banking.repositories;

import com.testproject.banking.dto.CardInfoDto;
import com.testproject.banking.entities.Card;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {

    @Query("SELECT c.balance " +
            "FROM Card c " +
            "WHERE c.cardNumber = :cardNumber")
    BigDecimal getCardBalance(String cardNumber);

    @Modifying
    @Transactional
    @Query("UPDATE Card c " +
            "SET c.balance = :newBalance " +
            "WHERE c.cardNumber = :cardNumber")
    void updateCardBalance(String cardNumber, BigDecimal newBalance);

    boolean existsByCardNumber(String cardNumber);

    @Query("SELECT c.user.username " +
            "FROM Card c " +
            "WHERE c.cardNumber = :cardNumber")
    String getCardOwnerUsername(String cardNumber);

    @Query("SELECT new com.testproject.banking.dto.CardInfoDto(c.cardNumber, c.balance)" +
            "FROM Card c ")
    List<CardInfoDto> getAllCards();
}
