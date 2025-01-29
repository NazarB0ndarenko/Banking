package com.testproject.banking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDto {

    private String username;
    @Size(min = 8, max = 20)
    private String password;

    private String name;
    private String surname;

    @Email
    private String email;

    private Date birthDate;
}
