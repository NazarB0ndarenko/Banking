package com.testproject.banking.repositories;

import com.testproject.banking.dto.LoginUserDto;
import com.testproject.banking.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class UserRepositoryTest {

    @Container
    private static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0");

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

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
