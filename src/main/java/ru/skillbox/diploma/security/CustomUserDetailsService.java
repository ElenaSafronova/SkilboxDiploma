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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(@NotNull String email) throws UsernameNotFoundException {
        logger.info("String email is " + email);

//        User petrov = userService.findUserByEmail("petrov@mail.ru");
//        System.out.println(petrov.getName());
//
//        CustomUserDetails petrovInMemory =  CustomUserDetails.builder()
//                .username(petrov.getEmail())
//                .password(petrov.getPassword())
//                .authorities(new ArrayList<>())
//                .isAccountNonExpired(true)
//                .isAccountNonLocked(true)
//                .isCredentialsNonExpired(true)
//                .isEnabled(true)
//                .isModerator(petrov.getIsModerator() == 1)
//                .build();
//        System.out.println(petrovInMemory.getUsername() + petrovInMemory.getPassword() + petrovInMemory.isModerator());
//        return petrovInMemory;

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
        logger.info("UserDetails buildUserForAuthentication: " + curUser.toString());
        return new org.springframework.security.core.userdetails.User(
                curUser.getName(),
                curUser.getPassword(),
                curUser.getIsModerator() == 1,
                true, true,
                true, new ArrayList<>());
    }
}
