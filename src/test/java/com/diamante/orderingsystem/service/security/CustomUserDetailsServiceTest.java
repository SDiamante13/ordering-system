package com.diamante.orderingsystem.service.security;

import com.diamante.orderingsystem.entity.User;
import com.diamante.orderingsystem.repository.security.UserRepository;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    private final UserRepository mockUserRepository = mock(UserRepository.class);

    private final CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(mockUserRepository);

    private final String username = "bob123";
    private final String password = "f4Q98#bea";

    private final User authenticatedUser = User.builder()
            .username(username)
            .password(password)
            .roles(Collections.singletonList("ROLE_USER"))
            .build();

    @Test
    public void loadUserByUsername() {
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(authenticatedUser));

        UserDetails result = customUserDetailsService.loadUserByUsername(username);

        assertThat(result).isEqualTo(authenticatedUser);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameFails_thenThrowUsernameNotFoundException() {
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        customUserDetailsService.loadUserByUsername(username);
    }
}