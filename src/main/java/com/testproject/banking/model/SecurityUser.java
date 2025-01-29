package com.testproject.banking.model;


import com.testproject.banking.dto.LoginUserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class SecurityUser implements UserDetails {
    private LoginUserDto loginDto;

    @Override
    public String getPassword() {
        return loginDto.getPassword();
    }

    @Override
    public String getUsername() {
        return loginDto.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "");
    }

}
