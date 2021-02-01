package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diploma.Dto.*;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.service.CaptchaService;
import ru.skillbox.diploma.service.PostService;
import ru.skillbox.diploma.service.UserService;
import ru.skillbox.diploma.value.PostStatus;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private Map<String, Integer> sessionMap = new ConcurrentHashMap<>();

    @GetMapping("/check")
    public ResponseEntity<String> checkAuthorization(Model model){
        logger.trace("Request /api/auth/check");
        model.addAttribute("result", false);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaDto> getCaptcha() throws IOException {
        CaptchaDto captchaDto = captchaService.generateCaptcha();
        return new ResponseEntity<>(captchaDto, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping (value = "/login", method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<AuthenticationDto> create(@RequestBody UserDataDto userDataDto) {
        logger.trace("/api/auth/login");
        User actualUser = userService.findUserByEmailAndPassword(userDataDto.getE_mail(), userDataDto.getPassword());
        if (actualUser == null){
            return new ResponseEntity<>(new AuthenticationDto(null), HttpStatus.OK);
        }
        UserDataAuthDto userDataAuthDto = new UserDataAuthDto(actualUser);
        if (userDataAuthDto.getModerationCount() > 0){
            logger.trace("postService.countByModerationStatus(PostStatus.NEW)");
            userDataAuthDto.setModerationCount(postService.countByModerationStatus(PostStatus.NEW));
        }
        // TODO: Если пользователь авторизован, идентификатор его сессии должен запоминаться в
        //  Map<String, Integer> со значением, равным ID пользователя, которому принадлежит данная сессия.

        return new ResponseEntity<>(new AuthenticationDto(userDataAuthDto), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<AuthenticationFailedDto> logOut(){
        logger.trace("/api/auth/logout");
        return new ResponseEntity<>(new AuthenticationFailedDto(true), HttpStatus.OK);
    }
}

