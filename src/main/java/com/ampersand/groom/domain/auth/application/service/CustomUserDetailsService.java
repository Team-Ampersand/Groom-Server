package com.ampersand.groom.domain.auth.application.service;

import com.ampersand.groom.domain.auth.application.port.AuthPort;
import com.ampersand.groom.domain.auth.expection.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthPort authPort;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return authPort.findByEmail(email)
                .map(member -> {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());
                    return new User(member.getEmail(), member.getPassword(), Collections.singletonList(authority));
                })
                .orElseThrow(() -> new UserNotFoundException());
    }
}
