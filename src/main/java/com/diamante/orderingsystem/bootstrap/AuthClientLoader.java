package com.diamante.orderingsystem.bootstrap;

import com.diamante.orderingsystem.security.Client;
import com.diamante.orderingsystem.security.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Profile("auth")
@Slf4j
public class AuthClientLoader implements CommandLineRunner {

    private final ClientRepository clientRepository;

    private Client adminClient = Client.builder()
            .clientName("admin")
            .clientSecret("password")
            .roles(Collections.singletonList("ROLE_ADMIN"))
            .build();

    public AuthClientLoader(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        clientRepository.save(adminClient);
    }
}
