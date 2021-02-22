package ru.skillbox.diploma.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_MODERATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
