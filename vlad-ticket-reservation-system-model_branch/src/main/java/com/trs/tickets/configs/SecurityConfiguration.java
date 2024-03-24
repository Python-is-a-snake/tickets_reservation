package com.trs.tickets.configs;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    private final String[] staticResources  =  {
            "/css/**",
            "/images/**",
            "/fonts/**",
            "/scripts/**",
            "/webjars/**",
            "/js/**"
    };

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeHttpRequests().requestMatchers(staticResources).permitAll();

        http.authorizeHttpRequests().requestMatchers("/register", "/movies/**",
                "/", "/contact-us", "/error", "/test", "/doRegister").permitAll();

        http.authorizeHttpRequests().anyRequest().authenticated();
        
        http.formLogin(login -> login
                .loginPage("/login")
//                .defaultSuccessUrl("/movies", true)
                .permitAll());

        http.logout().permitAll();

        http.exceptionHandling().accessDeniedPage("/access-denied");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
