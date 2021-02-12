package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.dto.LoginDto;
import ru.skillbox.diploma.dto.UserDataAuthDto;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.value.PostStatus;

@Service
public class AuthService {
    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    public UserDataAuthDto login(LoginDto loginDto) {
        if (loginDto == null){
            return null;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String curEmail = loginDto.getEmail();
        String curPass = loginDto.getPassword();

        User actualUser = userService.findUserByEmail(curEmail);

        if (actualUser == null){
            logger.info("user " + curEmail + " not found");
            return null;
        }

        if (!encoder.matches(curPass, actualUser.getPassword())){
            logger.info(curEmail + " password is wrong. " + curPass);
            return null;
        }
        UserDataAuthDto userDataAuthDto = new UserDataAuthDto(actualUser);
        if (userDataAuthDto.getModerationCount() > 0){
            logger.trace("postService.countByModerationStatus(PostStatus.NEW)");
            userDataAuthDto.setModerationCount(postService.countByModerationStatus(PostStatus.NEW));
        }
        return userDataAuthDto;
    }
}
