package ru.skillbox.diploma.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.service.UserService;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(@NotNull String email) throws UsernameNotFoundException {
        logger.info("!loadUserByUsername! String email is " + email);

        User curUser = userService.findUserByEmail(email);
        logger.debug("userRepository.findByEmail(" + email + ") result is " + curUser);
        if (curUser == null){
            throw new UsernameNotFoundException("User not found");
        }
        logger.debug("user found: " + curUser);

        return buildUserForAuthentication(curUser);
    }

    private UserDetails buildUserForAuthentication(User curUser) {
        logger.info("UserDetails buildUserForAuthentication: " + curUser.toString());
        return new org.springframework.security.core.userdetails.User(
                curUser.getEmail(),
                curUser.getPassword(),
                true,true, true,
                true, new ArrayList<>());
//        AuthorityUtils.createAuthorityList("USER")
    }
}
