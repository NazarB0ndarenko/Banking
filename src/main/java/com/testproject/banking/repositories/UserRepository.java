package com.testproject.banking.repositories;

import com.testproject.banking.dto.LoginUserDto;
import com.testproject.banking.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT new com.testproject.banking.dto.LoginUserDto(u.username, u.password) " +
            "FROM User u " +
            "WHERE u.username = :username")
    Optional<LoginUserDto> loadUserByUsername(@Param("username") String username);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean findUserByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username")
    boolean findUserByUsername(@Param("username") String username);

}
