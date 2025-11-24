package com.tripto.booking_app.service.user.impl;

import com.tripto.booking_app.entity.User;
import com.tripto.booking_app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        User user = null;
        try {
            user = userService.findByUsername(username)
                    .orElseThrow(() -> {
                        log.error("User not found with username: {}", username);
                        return new UsernameNotFoundException("User not found: " + username);
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.debug("User found: {}, roles: {}", user.getUsername(), user.getRoles());

        Set<GrantedAuthority> authorities = getAuthorities(user);

        return getUserDetails(user, authorities);
    }

    private UserDetails getUserDetails(User user, Set<GrantedAuthority> authorities) {
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.getIsActive())
                .build();
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        return user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
}
