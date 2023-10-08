package com.trs.tickets.configs;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, CEO;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
