package com.diamante.orderingsystem.config;

import com.diamante.orderingsystem.entity.User;
import com.diamante.orderingsystem.service.security.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class JwtTokenProviderTest {

    private CustomUserDetailsService mockCustomUserDetailsService = mock(CustomUserDetailsService.class);

    private String username = "chintu";
    private String password = "password";
    private List<String> roles = Collections.singletonList("ROLE_ADMIN");

    private User user = User.builder()
            .username(username)
            .password(password)
            .roles(roles)
            .build();

    private JwtTokenProvider jwtTokenProvider = spy(new JwtTokenProvider(mockCustomUserDetailsService));

    private String token = "I.AM.TOKEN";

    @Test
    public void createToken() {
        String token = jwtTokenProvider.createToken(username,
                roles);

        List<String> actualRoles = (List<String>) Jwts.parser().setSigningKey("secret")
                .parseClaimsJws(token).getBody().get("roles");

        assertThat(actualRoles).isEqualTo(roles);
    }

    @Test
    public void getAuthentication() {
        when(mockCustomUserDetailsService.loadUserByUsername(username)).thenReturn(user);
        doReturn(username).when(jwtTokenProvider).getUsername(token);

        UsernamePasswordAuthenticationToken expectedAuthenticationToken = new UsernamePasswordAuthenticationToken(user,
                "", user.getAuthorities());

        Authentication actualAuthenticationToken = jwtTokenProvider.getAuthentication(token);

        assertThat(actualAuthenticationToken).isEqualTo(expectedAuthenticationToken);
    }

    @Test
    public void resolveToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("Authorization", "Bearer " + token);

        String result = jwtTokenProvider.resolveToken(request);

        assertThat(result).isEqualTo(token);
    }

    @Test(expected = JwtException.class)
    public void validateToken() {
        jwtTokenProvider.validateToken(token);
    }
}