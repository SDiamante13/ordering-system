package com.diamante.orderingsystem.bootstrap;

import com.diamante.orderingsystem.entity.User;
import com.diamante.orderingsystem.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Profile("test")
@RequiredArgsConstructor
@Slf4j
@Component
public class AuthClientLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Value("${jwt.username}")
    private String username;

    @Value("${jwt.password}")
    private String password;

    private User adminUser;

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    @Override
    public void run(String... args) throws Exception {
        adminUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(Collections.singletonList("ROLE_ADMIN"))
                .build();


        userRepository.deleteAll();
        userRepository.save(adminUser);
    }
}
