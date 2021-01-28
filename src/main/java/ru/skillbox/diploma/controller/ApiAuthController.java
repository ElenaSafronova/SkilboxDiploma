package ru.skillbox.diploma.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.responce.CaptchaResponse;
import ru.skillbox.diploma.service.CaptchaService;
import ru.skillbox.diploma.service.PostService;
import ru.skillbox.diploma.service.UserService;
import ru.skillbox.diploma.value.PostStatus;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private PostService postService;

    Logger logger = LoggerFactory.getLogger(ApiAuthController.class);

    @GetMapping("/check")
    public ResponseEntity<String> checkAuthorization(Model model){
        logger.trace("Request /api/auth/check");
        model.addAttribute("result", false);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() throws IOException {
        CaptchaResponse captchaResponse = captchaService.generateCaptcha();
        return new ResponseEntity<>(captchaResponse, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping (value = "/login", method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Authentication> create(@RequestBody UserData userData) {
        logger.trace("/api/auth/login");
        User actualUser = userService.findUserByEmailAndPassword(userData.getE_mail(), userData.getPassword());
        if (actualUser == null){
            return new ResponseEntity<>(new Authentication(null), HttpStatus.OK);
        }
        UserDataResponce userDataResponce = new UserDataResponce(actualUser);
        if (userDataResponce.getModerationCount() > 0){
            logger.trace("postService.countByModerationStatus(PostStatus.NEW)");
            userDataResponce.setModerationCount(postService.countByModerationStatus(PostStatus.NEW));
        }
        return new ResponseEntity<>(new Authentication(userDataResponce), HttpStatus.OK);
    }
}

@NoArgsConstructor
@Data
class UserData{
    private String e_mail;
    private String password;
}

@Data
class Authentication{
    private boolean result;
    private UserDataResponce user;

    public Authentication(UserDataResponce userDataResponce) {
        this.user = userDataResponce;
        this.result = userDataResponce != null;
    }
}

@AllArgsConstructor
@Data
class AuthenticationFailed{
    private boolean result = false;
}

@Data
class UserDataResponce{
    private int id;
    private String name;
    private String photo;
    private String e_mail;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;

    public UserDataResponce(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.photo = user.getPhoto();
        this.e_mail = user.getEmail();
        this.moderation = user.getIsModerator() == 1;
        this.moderationCount = moderation ? 1 : 0;
        this.settings = moderation;
    }
}

