package ru.skillbox.diploma.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;


    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("loadUserByUsername class");
        logger.info("String email is " + email);

        User curUser = userService.findUserByEmail(email);
        logger.debug("userRepository.findByEmail(" + email + ") result is " + curUser);
        if (curUser == null){
            throw new UsernameNotFoundException("User not found");
        }
        logger.debug("user found: " + curUser);

        return buildUserForAuthentication(curUser);

//        CustomUserDetails curUserDetails = new CustomUserDetails(curUser);
//        return curUserDetails;
    }

    private UserDetails buildUserForAuthentication(User curUser) {
        return new org.springframework.security.core.userdetails.User(
                curUser.getName(),
                curUser.getPassword(),
                true, true, true,
                true, new ArrayList<>());
    }
}
