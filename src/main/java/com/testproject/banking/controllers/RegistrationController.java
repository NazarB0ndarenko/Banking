package com.testproject.banking.controllers;

import com.testproject.banking.dto.CreateUserDto;
import com.testproject.banking.entities.Card;
import com.testproject.banking.services.CardService;
import com.testproject.banking.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/login")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final CardService cardService;

    public Card createUser(@RequestBody CreateUserDto createUserDto) {
        long userId = userService.createUser(createUserDto);
        return cardService.createCard(userId);
    }
}
