package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import ru.skillbox.diploma.dto.AuthenticationDto;
import ru.skillbox.diploma.dto.LoginDto;
import ru.skillbox.diploma.dto.UserDataAuthDto;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.value.PostStatus;

import java.util.Hashtable;
import java.util.Map;

@Service
public class AuthService {
    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    Map<String, Integer> loggedUsers = new Hashtable<>();

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

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        logger.info("authentication.getDetails(): " + authentication.getDetails());

        String sessId = getSessionId();

        if(loggedUsers.containsKey(sessId)){
            logger.info("session already exists: " + sessId + " update userId " + actualUser.getId());
        }
        else{
            logger.info("new logged user: session " + sessId + " userId " + actualUser.getId());
        }
        loggedUsers.put(sessId, actualUser.getId());
        printLoggedUsers();

        UserDataAuthDto userDataAuthDto = new UserDataAuthDto(actualUser);
        if (userDataAuthDto.getModerationCount() > 0){
            logger.trace("postService.countByModerationStatus(PostStatus.NEW)");
            userDataAuthDto.setModerationCount(postService.countByModerationStatus(PostStatus.NEW));
        }
        return userDataAuthDto;
    }

    public AuthenticationDto checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("authentication.getDetails(): " + authentication.getDetails());

        logger.info("authentication" + authentication.toString());
        if (authentication.getPrincipal().equals("anonymousUser")) {
            logger.info("--- anonymousUser");
//            return new AuthenticationDto(null);
        }
//        User curUser = (User)authentication.getPrincipal();
//        logger.info("--- curUser: " + curUser);

        String sessId = getSessionId();
        logger.info("sessId: " + sessId);
        if(loggedUsers.containsKey(sessId)){
            User curUser = userService.findById(loggedUsers.get(sessId));
            logger.debug("curUser: " + curUser.getName() + " " + curUser.getEmail());
            return new AuthenticationDto(new UserDataAuthDto(curUser));
        }
        return new AuthenticationDto(null);
    }

    public boolean logout() {
        String sessionId = getSessionId();
        if (loggedUsers.containsKey(sessionId)){
            loggedUsers.remove(sessionId);
            printLoggedUsers();
            return true;
        }
        return false;
    }

    private String getSessionId(){
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    private void printLoggedUsers(){
        if(loggedUsers.isEmpty()){
            System.out.println("NO USERS ONLINE");
        }
        for(Map.Entry<String, Integer> item : loggedUsers.entrySet()){
            System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue());
        }
    }
}
