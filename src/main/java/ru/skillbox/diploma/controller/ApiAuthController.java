package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diploma.dto.*;
import ru.skillbox.diploma.exception.EmailExistsException;
import ru.skillbox.diploma.service.AuthService;
import ru.skillbox.diploma.service.CaptchaService;
import ru.skillbox.diploma.service.PostService;
import ru.skillbox.diploma.service.UserService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    Logger logger = LoggerFactory.getLogger(ApiAuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private PostService postService;

    @GetMapping("/check")
    @MessageMapping("/login")
    public ResponseEntity<AuthenticationDto> checkAuthorization(
            @Header("simpSessionId") String sessionId,
            HttpSession session
    )
    {
        logger.trace("Request /api/auth/check");
        logger.trace("simpSessionId: " + sessionId);
        logger.trace("HttpSessionId: " + session.getId());

        return new ResponseEntity<>(authService.checkAuthentication(), HttpStatus.OK);
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
        UserDataAuthDto userDataAuthDto = authService.login(loginDto);

        return new ResponseEntity<>(new AuthenticationDto(userDataAuthDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationDto> registerUser(@RequestBody NewUserDataDto newUserDataDto) throws EmailExistsException {
        logger.trace("/api/auth/register");
        RegistrationDto registrationDto = userService.registerNewUser(newUserDataDto);
        return new ResponseEntity(registrationDto, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<AuthenticationFailedDto> logOut() throws URISyntaxException {
        logger.trace("/api/auth/logout");
        authService.logout();

//        URI uri = new URI("/");
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(uri);
//        return new ResponseEntity<>(new AuthenticationFailedDto(true), HttpStatus.OK);

//        HttpServletResponse response = null;
//        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
//        response.setHeader("Location", "/api/post");

        return new ResponseEntity<>(new AuthenticationFailedDto(true), HttpStatus.OK);
    }
}

