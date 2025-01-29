package com.testproject.banking.controllers;

import com.testproject.banking.dto.CardDto;
import com.testproject.banking.dto.CreateUserDto;
import com.testproject.banking.entities.Card;
import com.testproject.banking.exceptions.UserAlreadyExistsException;
import com.testproject.banking.services.CardService;
import com.testproject.banking.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign-up")
@AllArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final CardService cardService;

    @PostMapping("/user")
    public ResponseEntity<CardDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {

        if (userService.userExists(createUserDto.getUsername(), createUserDto.getEmail())) {
            throw new UserAlreadyExistsException("User already exists");
        }

        long userId = userService.createUser(createUserDto);
        return ResponseEntity.ok().body(cardService.createCard(userId));
    }
}

