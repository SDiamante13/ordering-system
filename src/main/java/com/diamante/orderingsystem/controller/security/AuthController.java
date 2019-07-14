package com.diamante.orderingsystem.controller.security;

import com.diamante.orderingsystem.config.JwtTokenProvider;
import com.diamante.orderingsystem.entity.AuthRequest;
import com.diamante.orderingsystem.entity.User;
import com.diamante.orderingsystem.service.security.CustomUserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping("/security")
@Api(tags = {"Security Token API"})
@SwaggerDefinition(tags = {@Tag(name = "Security Token API",
        description = "Provides authenticated users with JWT tokens to use in subsequent API calls.")})
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @ApiOperation(value = "Returns an auth token for authenticated users.")
    @PostMapping
    public ResponseEntity authorize(@RequestBody AuthRequest authRequest) {
        try {
            String username = authRequest.getUsername();
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, authRequest.getPassword());
            authenticationManager.authenticate(authReq);
            User user = ((User) customUserDetailsService.loadUserByUsername(username));
            String token = jwtTokenProvider.createToken(username, user.getRoles());
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied. " + e.getMessage());
        }
    }
}
