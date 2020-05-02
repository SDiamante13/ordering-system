package com.diamante.orderingsystem.controller.security;

import com.diamante.orderingsystem.config.JwtTokenProvider;
import com.diamante.orderingsystem.entity.User;
import com.diamante.orderingsystem.service.security.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AuthController.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JwtTokenProvider mockJwtTokenProvider;

    @MockBean
    private AuthenticationManager mockAuthenticationManager;

    @MockBean
    private CustomUserDetailsService mockCustomUserDetailsService;

    private String username = "bob123";
    private String password = "f4Q98#bea";
    private String validToken = "eyBVal1dTok3n";

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void postWithBodyOfValidAuthRequest_shouldReturnAValidJWTToken() throws Exception {
        User authenticatedUser = User.builder()
                .username(username)
                .password(password)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        when(mockCustomUserDetailsService.loadUserByUsername(username)).thenReturn(authenticatedUser);
        when(mockJwtTokenProvider.createToken(any(), any())).thenReturn(validToken);

        mockMvc.perform(post("/security")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authenticatedUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(username)))
                .andExpect(jsonPath("$.token", isA(String.class)));
    }

    @Test
    public void postWithBodyOfInvalidAuthRequest_shouldReturn403() throws Exception {
        User authenticatedUser = User.builder()
                .username(username)
                .password(password)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        doThrow(new UsernameNotFoundException("Username " + username + " was not found."))
                .when(mockCustomUserDetailsService).loadUserByUsername(username);

        mockMvc.perform(post("/security")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authenticatedUser)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Invalid username/password supplied. Username bob123 was not found.")));
    }
}