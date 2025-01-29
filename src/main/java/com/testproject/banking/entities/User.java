package com.testproject.banking.entities;

import com.testproject.banking.dto.CreateUserDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String name;
    private String surname;
    private String email;

    @Column(name = "birth_day")
    private Date birthDay;
    private Timestamp createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Card card;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    public User(CreateUserDto createUserDto) {
        this.name = createUserDto.getName();
        this.surname = createUserDto.getSurname();
        this.email = createUserDto.getEmail();
        this.birthDay = createUserDto.getBirthDate();
        this.username = createUserDto.getUsername();
        this.password = createUserDto.getPassword();
    }

}

