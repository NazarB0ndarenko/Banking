package com.testproject.banking.servises;

import com.testproject.banking.entities.Card;
import com.testproject.banking.entities.User;
import com.testproject.banking.dto.CreateUserDto;
import com.testproject.banking.dto.LoginDto;
import com.testproject.banking.model.SecurityUser;
import com.testproject.banking.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private CardService cardService;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        log.info("Loading user by id: {}", id);

        LoginDto loginDto = userRepository.findUserById(Long.parseLong(id))
                .orElseThrow(() -> {
                    log.error("User not found");
                    return new UsernameNotFoundException("User not found");
                });

        return new SecurityUser(loginDto);
    }

    public void createUser(CreateUserDto createUserDto) {
        log.info("Encoding password for user with username: {}", createUserDto.getUsername());
        createUserDto.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        log.info("Creating user with username: {}", createUserDto.getUsername());
        User user = new User(createUserDto);

        log.info("Creating card for user with username: {}", createUserDto.getUsername());
        Card card = cardService.createCard(createUserDto.getName() + createUserDto.getSurname());
        

        log.info("Saving user with username: {}", createUserDto.getUsername());
        userRepository.save(user);



    }
}
