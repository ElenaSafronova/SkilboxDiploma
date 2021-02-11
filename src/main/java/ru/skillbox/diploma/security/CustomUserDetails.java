package ru.skillbox.diploma.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skillbox.diploma.model.User;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    Logger logger = LoggerFactory.getLogger(CustomUserDetails.class);

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
        logger.debug("CustomUserDetails: " + user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
//        return AuthorityUtils.createAuthorityList("VALID_USER");
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserNameAndPassword() {
        return user.getName() + " " + user.getPassword();
    }
}
