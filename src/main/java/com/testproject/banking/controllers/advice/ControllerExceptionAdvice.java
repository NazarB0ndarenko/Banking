package com.testproject.banking.controllers.advice;

import com.testproject.banking.exceptions.CardNotFoundException;
import com.testproject.banking.exceptions.NotEnoughFoundsException;
import com.testproject.banking.exceptions.UserAlreadyExistsException;
import com.testproject.banking.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> userExistHandler(){
        ErrorDetails errorDetails = new ErrorDetails("Username or email already in use");
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetails> authenticationExceptionHandler(){
        ErrorDetails errorDetails = new ErrorDetails("Login failed: Invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }

    @ExceptionHandler(NotEnoughFoundsException.class)
    public ResponseEntity<ErrorDetails> notEnoughFoundsExceptionHandler(){
        ErrorDetails errorDetails = new ErrorDetails("Not enough founds");
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> illegalArgumentExceptionHandler(){
        ErrorDetails errorDetails = new ErrorDetails("Amount must be positive");
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ErrorDetails> cardNotFoundExceptionHandler(){
        ErrorDetails errorDetails = new ErrorDetails("Card not found");
        return ResponseEntity.badRequest().body(errorDetails);
    }

}
