package com.testproject.banking.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.testproject.banking.dto.CreateUserDto;
import com.testproject.banking.dto.LoginUserDto;
import com.testproject.banking.entities.User;
import com.testproject.banking.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.sql.Date;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUserExists_whenUserExists() {
        String username = "testUser";
        String email = "test@example.com";

        when(userRepository.findUserByUsername(username)).thenReturn(true);
        when(userRepository.findUserByEmail(email)).thenReturn(false);

        boolean result = userService.userExists(username, email);

        assertThat(result).isTrue();
        verify(userRepository).findUserByUsername(username);
        verify(userRepository).findUserByEmail(email);
    }

    @Test
    public void testUserExists_whenEmailExists() {
        String username = "testUser";
        String email = "test@example.com";

        when(userRepository.findUserByUsername(username)).thenReturn(false);
        when(userRepository.findUserByEmail(email)).thenReturn(true);

        boolean result = userService.userExists(username, email);

        assertThat(result).isTrue();
        verify(userRepository).findUserByUsername(username);
        verify(userRepository).findUserByEmail(email);
    }

    @Test
    public void testUserExists_whenNeitherExists() {
        String username = "testUser";
        String email = "test@example.com";

        when(userRepository.findUserByUsername(username)).thenReturn(false);
        when(userRepository.findUserByEmail(email)).thenReturn(false);

        boolean result = userService.userExists(username, email);

        assertThat(result).isFalse();
        verify(userRepository).findUserByUsername(username);
        verify(userRepository).findUserByEmail(email);
    }

    @Test
    public void testLoadUserByUsername_whenUserExists() {
        String username = "testUser";
        LoginUserDto userDto = new LoginUserDto(username, "encodedPassword");

        when(userRepository.loadUserByUsername(username)).thenReturn(Optional.of(userDto));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).loadUserByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_whenUserDoesNotExist() {
        String username = "nonExistentUser";

        when(userRepository.loadUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
        verify(userRepository).loadUserByUsername(username);
    }

    @Test
    public void testCreateUser() {
        Date birthDate = Date.valueOf("1990-01-01");

        CreateUserDto createUserDto = new CreateUserDto(
                "testUser",
                "rawPassword",
                "John",
                "Doe",
                "john.doe@example.com",
                birthDate
        );

        User savedUser = new User(createUserDto);
        savedUser.setId(1L);

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        long userId = userService.createUser(createUserDto);

        assertThat(userId).isEqualTo(1L);
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(any(User.class));
    }

//    @Test
//    public void testCreateApiUser() {
//        CreateApiDto createApiDto = new CreateApiDto("apiUser", "apiPassword", "apiEmail@test.com");
//
//        User savedUser = new User(createApiDto);
//        savedUser.setId(1L);
//
//        when(passwordEncoder.encode("apiPassword")).thenReturn("encodedPassword");
//        when(userRepository.save(any(User.class))).thenReturn(savedUser);
//
//        UserDetails userDetails = userService.createApiUser(createApiDto);
//
//        assertThat(userDetails).isNotNull();
//        assertThat(userDetails.getUsername()).isEqualTo("apiUser");
//        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword"); // Ensure encoded password
//        verify(passwordEncoder).encode("apiPassword");
//        verify(userRepository).save(any(User.class));
//    }
}
