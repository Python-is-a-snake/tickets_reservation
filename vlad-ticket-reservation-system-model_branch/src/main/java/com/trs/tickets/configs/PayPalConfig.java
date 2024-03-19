package com.trs.tickets.configs;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {
    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public APIContext apiContext(){
//        return new APIContext(clientId, clientSecret, mode);
        return new APIContext("AXlyCOAHPLVnQncPXl5AJnnCwECILK4q0k0quKedqED3bQwSKNeefQDrNf6LespGutO9j-D1XqXbiAEC",
                "EIaBqugZwDjD2n6y08Emf1BkiD9dNVd_kfLCERLmUvitRuGhLmGINY4RSSyXDSNEg7Jzzrvb4kWqL4ZL", "sandbox");
    }
}
