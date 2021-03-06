package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import ru.skillbox.diploma.model.dto.*;
import ru.skillbox.diploma.model.exception.EmailExistsException;
import ru.skillbox.diploma.model.entity.Captcha;
import ru.skillbox.diploma.model.entity.User;
import ru.skillbox.diploma.model.value.PostStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private User curUser = null;
    private Map<String, Integer> loggedUsers = new Hashtable<>();

    public User getCurUser() {
        return curUser;
    }

    public UserDataAuthDto login(LoginDto loginDto) {
        if (loginDto == null){
            LOGGER.info("loginDto is null");
            return null;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String curEmail = loginDto.getEmail();
        String curPass = loginDto.getPassword();

        User actualUser = userService.findUserByEmail(curEmail);

        if (actualUser == null){
            LOGGER.info("user " + curEmail + " not found");
            return null;
        }

        if (!encoder.matches(curPass, actualUser.getPassword())){
            LOGGER.info(curEmail + " password is wrong. " + curPass);
            return null;
        }

        String sessId = getSessionId();

        if(loggedUsers.containsKey(sessId)){
            LOGGER.info("session already exists: " + sessId + " update userId " + actualUser.getId());
        }
        else{
            LOGGER.info("new logged user: session " + sessId + " userId " + actualUser.getId());
        }
        loggedUsers.put(sessId, actualUser.getId());
        printLoggedUsers();

        UserDataAuthDto userDataAuthDto = new UserDataAuthDto(actualUser);
        System.out.println("------------ userDataAuthDto " + userDataAuthDto);
        userDataAuthDto
                .setModerationCount(checkModerationCount(userDataAuthDto.getModerationCount()));

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(curEmail, curPass)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        curUser = userService.findUserByEmail(user.getUsername());
        LOGGER.info("!!! authorized user " + user.getUsername());

        return userDataAuthDto;
    }

    public AuthenticationDto checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("authentication" + authentication.toString());
        if (authentication.getPrincipal().equals("anonymousUser")) {
            LOGGER.info("--- anonymousUser");
            return new AuthenticationDto(null);
        }

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserEmail = authentication.getName();
            LOGGER.info("authentication.getName(): " + currentUserEmail);
            UserDataAuthDto userDataAuthDto = new UserDataAuthDto(userService.findUserByEmail(currentUserEmail));
            userDataAuthDto
                    .setModerationCount(checkModerationCount(userDataAuthDto.getModerationCount()));
            return new AuthenticationDto(userDataAuthDto);
        }

        String sessId = getSessionId();
        LOGGER.info("sessId: " + sessId);
        if(loggedUsers.containsKey(sessId)){
            User curUser = userService.findById(loggedUsers.get(sessId));
            LOGGER.debug("curUser from Map: " + curUser.getName() + " " + curUser.getEmail());
            return new AuthenticationDto(new UserDataAuthDto(curUser));
        }
        return new AuthenticationDto(null);
    }

    private int checkModerationCount(int moderationCount) {
        if (moderationCount == 1){
            LOGGER.trace("postService.countByModerationStatus(PostStatus.NEW)");
            return postService.countByModerationStatus(PostStatus.NEW);
        }
        return moderationCount;
    }

    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            new SecurityContextLogoutHandler().logout(request, response, authentication);

        String sessionId = getSessionId();
        if (loggedUsers.containsKey(sessionId)){
            loggedUsers.remove(sessionId);
            printLoggedUsers();
            return true;
        }
        return false;
    }

    public Integer getCurUserId(){
        String sessId = getSessionId();
        if(loggedUsers.containsKey(sessId)){
            User curUser = userService.findById(loggedUsers.get(sessId));
            LOGGER.debug("curUser: " + curUser.getName() + " " + curUser.getEmail());
            return curUser.getId();
        }
        return null;
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

    public ResultAndErrorDto registerNewUser(NewUserDataDto newUserDataDto) throws EmailExistsException {
        Map<String, String> errors = new HashMap<>();

        if (userService.findUserByEmail(newUserDataDto.getEmail()) == null){
            if (!newUserDataDto.getName().matches("^([a-zA-ZА-Яа-я]{2,}\\s?([a-zA-ZА-Яа-я]{2,})?)")){
                errors.put("name", "Имя указано неверно");
            }
            if(newUserDataDto.getPassword().length() < 6){
                errors.put("password", "Пароль короче 6-ти символов");
            }
            Captcha captcha = captchaService.findBySecretCode(newUserDataDto.getCaptchaSecret());
            System.out.println("--------------- captcha " + captcha.toString());
            if (captcha == null){
                LOGGER.debug("captcha Код не найден в БД");
            }
            if (!captcha.getCode().equals(newUserDataDto.getCaptcha())){
                errors.put("captcha", "Код с картинки введён неверно");
            }

            if (errors.size() > 0){
                return new ResultAndErrorDto(false, errors);
            }

            User newUser = new User(
                    (byte) 0,
                    newUserDataDto.getName(),
                    newUserDataDto.getEmail(),
                    new BCryptPasswordEncoder().encode(newUserDataDto.getPassword())
            );
            userService.save(newUser);

            LOGGER.debug(newUser.toString());
            return new ResultAndErrorDto(true, null);
        }

        errors.put("email", "Этот e-mail уже зарегистрирован");
        return new ResultAndErrorDto(false, errors);
//        throw new EmailExistsException(newUserDataDto.getEmail());
    }
}
