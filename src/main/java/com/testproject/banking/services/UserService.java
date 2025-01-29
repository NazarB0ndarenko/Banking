package com.testproject.banking.services;

import com.testproject.banking.dto.LoginUserDto;
import com.testproject.banking.entities.User;
import com.testproject.banking.dto.CreateUserDto;
import com.testproject.banking.model.SecurityUser;
import com.testproject.banking.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public boolean userExists(String username, String email) {
        boolean userExists = userRepository.findUserByUsername(username);
        boolean emailExists = userRepository.findUserByEmail(email);

        return userExists || emailExists;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);

        LoginUserDto mainInfoUserDto = userRepository.loadUserByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found");
                    return new UsernameNotFoundException("User not found");
                });

        return new SecurityUser(mainInfoUserDto);
    }

    public long createUser(CreateUserDto createUserDto) {
        log.info("Encoding password for user with username: {}", createUserDto.getUsername());
        createUserDto.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        log.info("Creating user with username: {}", createUserDto.getUsername());
        User user = new User(createUserDto);

        log.info("Saving user with username: {}", createUserDto.getUsername());
        user = userRepository.save(user);
        return user.getId();
    }
}
