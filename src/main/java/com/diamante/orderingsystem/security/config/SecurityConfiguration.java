package com.diamante.orderingsystem.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    private static final String BASE_PATH = "api/v1";
    private static final String AUTH_PATH = "/security";
    private static final String WILDCARD = "/**";
    private static final String ADMIN = "ADMIN";

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(BASE_PATH + AUTH_PATH).permitAll()
                .antMatchers(HttpMethod.GET, BASE_PATH + WILDCARD).permitAll()
//                .antMatchers(HttpMethod.POST, BASE_PATH + WILDCARD).hasRole(ADMIN)
                .antMatchers(HttpMethod.PUT, BASE_PATH + WILDCARD).hasRole(ADMIN)
                .antMatchers(HttpMethod.DELETE, BASE_PATH + WILDCARD).hasRole(ADMIN)
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));


        // TODO restrict customer GET endpoint with USER role
        // TODO restrict order GET endpoint with USER role

    }
}