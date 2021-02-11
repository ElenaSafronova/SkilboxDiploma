package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diploma.Dto.*;
import ru.skillbox.diploma.exception.EmailExistsException;
import ru.skillbox.diploma.model.User;
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
    public ResponseEntity<CaptchaDto> getCaptcha() throws IOException {
        CaptchaDto captchaDto = captchaService.generateCaptcha();
        return new ResponseEntity<>(captchaDto, HttpStatus.OK);
    }

    @GetMapping("/login")
    public void login(){
        logger.trace("GET /api/auth/login");
        logger.trace("login?error");
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> authUser(@RequestBody LoginDto loginDto) {
        logger.trace("POST /api/auth/login");
        logger.trace("loginDto = " + loginDto.toString());

        if (loginDto == null){
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String curEmail = loginDto.getE_mail();
        String curPass = loginDto.getPassword();

        User actualUser = userService.findUserByEmail(curEmail);

        if (actualUser == null){
            logger.info("user " + curEmail + " not found");
            return new ResponseEntity<>(new AuthenticationDto(null), HttpStatus.OK);
        }

        if (!encoder.matches(curPass, actualUser.getPassword())){
            logger.info(curEmail + " password is wrong. " + curPass);
            return new ResponseEntity<>(new AuthenticationDto(null), HttpStatus.OK);
        }
        UserDataAuthDto userDataAuthDto = new UserDataAuthDto(actualUser);
        if (userDataAuthDto.getModerationCount() > 0){
            logger.trace("postService.countByModerationStatus(PostStatus.NEW)");
            userDataAuthDto.setModerationCount(postService.countByModerationStatus(PostStatus.NEW));
        }

        return new ResponseEntity<>(new AuthenticationDto(userDataAuthDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationDto> registerUser(@RequestBody newUserDataDto newUserDataDto) throws EmailExistsException {
        logger.trace("/api/auth/register");
        RegistrationDto registrationDto = userService.registerNewUser(newUserDataDto);
        return new ResponseEntity(registrationDto, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<AuthenticationFailedDto> logOut(){
        logger.trace("/api/auth/logout");
        return new ResponseEntity<>(new AuthenticationFailedDto(true), HttpStatus.OK);
    }
}

