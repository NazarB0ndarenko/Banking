package com.testproject.banking.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String  cardNumber;

    @Column(nullable = false)
    private int cvv;
    private Timestamp createdAt;

    private BigDecimal balance;

    @OneToOne
    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Card (String cardNumber, int cvv, long userId) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.balance = new BigDecimal(0);

        User user = new User();
        user.setId(userId);
        this.user = user;
    }
}
