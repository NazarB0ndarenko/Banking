package com.testproject.banking.repositories;

import com.testproject.banking.dto.LoginUserDto;
import com.testproject.banking.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");
        userRepository.save(user);

        Optional<LoginUserDto> result = userRepository.loadUserByUsername("testUsername");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testUsername");
        assertThat(result.get().getPassword()).isEqualTo("testPassword");
    }

    @Test
    public void testFindUserByEmail() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");
        userRepository.save(user);

        boolean exists = userRepository.findUserByEmail("test@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    public void testFindUserByUsername() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");
        userRepository.save(user);

        boolean exists = userRepository.findUserByUsername("testUsername");

        assertThat(exists).isTrue();
    }
}
